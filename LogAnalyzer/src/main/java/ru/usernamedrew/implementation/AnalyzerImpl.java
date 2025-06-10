package ru.usernamedrew.implementation;

import ru.usernamedrew.api.Analyzer;
import ru.usernamedrew.api.Operation;
import ru.usernamedrew.api.Parser;
import ru.usernamedrew.implementation.operations.BalanceInquiry;
import ru.usernamedrew.implementation.operations.Receive;
import ru.usernamedrew.implementation.operations.Transfer;
import ru.usernamedrew.implementation.operations.Withdrawal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

public class AnalyzerImpl implements Analyzer {
    private final Path inputPath;
    private final Path outputPath;
    private final Parser parser;
    private final Map<String, List<Event>> userLogs = new HashMap<>();

    public AnalyzerImpl(Path inputPath, Path outputPath, Parser parser) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.parser = parser;
    }

    @Override
    public void process() throws IOException {
        Files.createDirectories(outputPath);

        try (Stream<Path> paths = Files.list(inputPath)) {
            paths.filter(path -> path.toString().endsWith(".log")).forEach(this::processFile);
        }

        userLogs.forEach((key, value) -> {
            value.sort(Comparator.comparing(Event::getCreatedAt));
            writeToFile(key, value);
        });
    }

    private void processFile(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            lines.map(parser::parse).forEach(e -> {
                processUserEvent(e);

                if (e.getOperation() instanceof Transfer transfer) {
                    processUserEvent(new Event(e.getCreatedAt(), new Receive(transfer.getAmount(), e.getUser()), transfer.getRecipient()));
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void processUserEvent(Event event) {
        userLogs.computeIfAbsent(event.getUser(), k -> new ArrayList<>()).add(event);
    }

    private void writeToFile(String user, List<Event> events) {
        Path path = outputPath.resolve(user + ".log");
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path))) {
            for (Event event : events) {
                writer.println(event.toString());
            }
            writer.println(String.format("[%s] %s final balance %s", Parser.formatter.format(LocalDateTime.now()), user, calculateCurrentBalance(events)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private BigDecimal calculateCurrentBalance(List<Event> events) {
        BigDecimal balance = BigDecimal.ZERO;

        for (Event event : events) {
            Operation operation = event.getOperation();
            // TODO
            if (operation instanceof BalanceInquiry amount) {
                balance = amount.getAmount();
            } else if (operation instanceof Transfer transfer) {
                balance = balance.subtract(transfer.getAmount());
            } else if (operation instanceof Receive receive) {
                balance = balance.add(receive.getAmount());
            } else if (operation instanceof Withdrawal withdrew) {
                balance = balance.subtract(withdrew.getAmount());
            }
        }

        return balance;
    }
}
