package ru.usernamedrew;

import ru.usernamedrew.api.Analyzer;
import ru.usernamedrew.api.Parser;
import ru.usernamedrew.implementation.AnalyzerImpl;
import ru.usernamedrew.implementation.LogParser;

import java.io.IOException;
import java.nio.file.*;

public class App {
    public static void main( String[] args ) {
        if (args.length != 1) {
            System.err.println("You should add parameter <directory_with_logs>");
            System.exit(1);
        }

        Path logDir = Path.of(args[0]);

        Parser parser = new LogParser();
        Analyzer analyzer = new AnalyzerImpl(logDir, Path.of("transactions_by_users"), parser);
        try {
            analyzer.process();
        } catch (NoSuchFileException e) {
            System.err.println("No such directory exists: " + e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
