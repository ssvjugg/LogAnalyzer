package ru.usernamedrew.implementation;

import ru.usernamedrew.api.Analyzer;
import ru.usernamedrew.api.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class AnalyzerImpl implements Analyzer {
    private final Path inputPath;
    private final Path outputPath;
    private final Parser parser;
    private final Map<String, Event> userLogs = new HashMap<>();

    public AnalyzerImpl(Path inputPath, Path outputPath, Parser parser) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.parser = parser;
    }

    @Override
    public void process() throws IOException {
        Files.createDirectories(outputPath);

        try (Stream<Path> paths = Files.list(inputPath)) {
            paths.filter(path -> path.endsWith(".log")).forEach(this::processFile);
        }
    }

    private void processFile(Path path) {

    }

    @Override
    public void writeToFile(Path path) {

    }
}
