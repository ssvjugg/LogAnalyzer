package ru.usernamedrew.implementation;

import ru.usernamedrew.api.Analyzer;
import ru.usernamedrew.api.Parser;
import ru.usernamedrew.implementation.operations.Recive;
import ru.usernamedrew.implementation.operations.Transfer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
            writeToFile(outputPath.resolve(key + ".log"));
        });
    }

    private void processFile(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            lines.map(parser::parse).forEach(e -> {
                processUserEvent(e);

                if (e.getOperation() instanceof Transfer transfer) {
                    processUserEvent(new Event(e.getCreatedAt(), new Recive(transfer.getAmount(), e.getUser()), transfer.getRecipient()));
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void processUserEvent(Event event) {
        userLogs.computeIfAbsent(event.getUser(), k -> new ArrayList<>()).add(event);
    }

    private void writeToFile(Path path) {

    }
}
