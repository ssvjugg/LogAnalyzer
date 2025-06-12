package ru.usernamedrew.implementation;

import ru.usernamedrew.api.*;
import ru.usernamedrew.exeptions.*;
import ru.usernamedrew.implementation.operations.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

public final class AnalyzerImpl implements Analyzer {
    private final Path inputPath;
    private final Path outputPath;
    private final Parser parser;
    // For parallel execution we can use ConcurrentHashMap<String, CopyOnWriteArrayList<Event>> but for current size of logs it's unnecessary
    private final Map<String, List<Event>> userLogs = new HashMap<>();

    public AnalyzerImpl(Path inputPath, Path outputPath, Parser parser) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.parser = parser;
    }

    @Override
    public void process() throws IOException {
        Files.createDirectories(outputPath);

        // Log processing.
        try (Stream<Path> paths = Files.list(inputPath)) {
            paths.filter(path -> path.toString().endsWith(".log")).forEach(this::processFile);
        }

        // For all users, we are sort their logs and generate summary files.
        userLogs.forEach((key, value) -> {
            value.sort(Comparator.comparing(Event::getCreatedAt));
            writeToFile(key, value);
        });
    }

    private void processFile(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            lines.map(this::parseLineSafely).forEach(e -> {
                processUserEvent(e);

                // If the transaction was a Transfer we write it as a Receive to recipient
                if (e.getOperation() instanceof Transfer transfer) {
                    Operation receiveOperation = new Receive(transfer.getAmount(), e.getUser());

                    processUserEvent(new Event(e.getCreatedAt(), receiveOperation, transfer.getRecipient()));
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /*
        Helper method for processFile. This method can be considered a crutch,
        since in the era of lambdas and method references, checked exceptions are a headache.
     */
    private Event parseLineSafely(String line) {
        try {
            return parser.parse(line);
        } catch (NegativeAmountException | UnknownOperationException e) {
            var exception = new IllegalStateException(e.getMessage());
            exception.initCause(e);
            throw exception;
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

            // If we know the amount of balance at the moment, we shall update it, because necessary logs can be missed
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

    @Override
    public Path getInputPath() {
        return inputPath;
    }

    @Override
    public Path getOutputPath() {
        return outputPath;
    }
}
