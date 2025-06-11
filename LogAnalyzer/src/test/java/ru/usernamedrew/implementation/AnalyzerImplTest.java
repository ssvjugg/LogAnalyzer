package ru.usernamedrew.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.usernamedrew.api.Analyzer;
import ru.usernamedrew.api.Parser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

class AnalyzerImplTest {
    private Analyzer analyzer;

    @BeforeEach
    void setUp() throws IOException {
        Path inputPath = Path.of("logs/");
        Files.createDirectories(inputPath);
        Parser parser = new LogParser();
        analyzer = new AnalyzerImpl(inputPath, Path.of("transactions_by_users"), parser);
    }

    @AfterEach
    void tearDown() throws IOException {
        deleteLogFiles(Path.of("logs/"));
        deleteLogFiles(Path.of("transactions_by_users"));
    }

    private void deleteLogFiles(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return;
        }

        try (Stream<Path> files = Files.list(directory)) {
            files.filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".log"))
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            throw new UncheckedIOException("Failed to delete " + file, e);
                        }
                    });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    @Test
    void processTestRightLog() throws IOException {
        Path logPath = analyzer.getInputPath().resolve("log1.log");

        List<String> lines = List.of(
                "[2025-05-10 09:00:22] user001 balance inquiry 1000.00",
                "[2025-05-10 09:05:44] user001 transferred 100.00 to user002",
                "[2025-05-10 09:06:00] user001 transferred 120.00 to user002",
                "[2025-05-10 10:30:55] user005 transferred 10.00 to user003",
                "[2025-05-10 11:09:01] user001 transferred 235.54 to user004",
                "[2025-05-10 12:38:31] user003 transferred 150.00 to user002",
                "[2025-05-11 10:00:31] user002 balance inquiry 210.00"
        );

        Files.write(logPath, lines);

        analyzer.process();

        Path outputPath = analyzer.getOutputPath().resolve("user001.log");

        Assertions.assertTrue(Files.exists(outputPath));

        List<String> expectedLines = List.of(
                "[2025-05-10 09:00:22] user001 balance inquiry 1000.00",
                "[2025-05-10 09:05:44] user001 transferred 100.00 to user002",
                "[2025-05-10 09:06:00] user001 transferred 120.00 to user002",
                "[2025-05-10 11:09:01] user001 transferred 235.54 to user004",
                "\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\] user001 final balance 544.46"
        );

        Assertions.assertLinesMatch(expectedLines, Files.readAllLines(outputPath));
    }

    @Test
    void processTestWrongLog() throws IOException {
        Path logPath = analyzer.getInputPath().resolve("log1.log");

        List<String> lines = List.of(
                "[2025-05-10 09:00:22] user001 balance inquiry -1000.00",
                "[2025-05-10 09:05:44] user001 transferred 100.00 to user002",
                "[2025-05-10 09:06:00] user001 transferred 120.00 to user002",
                "[2025-05-10 10:30:55] user005 transferred 10.00 to user003",
                "[2025-05-10 11:09:01] user001 transferred 235.54 to user004",
                "[2025-05-10 12:38:31] user003 transferred 150.00 to user002",
                "[2025-05-11 10:00:31] user002 balance inquiry 210.00"
        );

        Files.write(logPath, lines);

        Assertions.assertThrows(IllegalStateException.class, () -> analyzer.process());
    }
}