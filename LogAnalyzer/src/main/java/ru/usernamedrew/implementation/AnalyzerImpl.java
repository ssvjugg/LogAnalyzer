package ru.usernamedrew.implementation;

import ru.usernamedrew.api.Analyzer;

import java.nio.file.Path;

public class AnalyzerImpl implements Analyzer {
    private final Path inputPath;
    private final Path outputPath;

    public AnalyzerImpl(Path inputPath, Path outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    private void processLogFiles(Path path) {

    }

    @Override
    public void processLogLines() {

    }

    @Override
    public void writeToFile(Path path) {

    }
}
