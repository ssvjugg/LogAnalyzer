package ru.usernamedrew;

import ru.usernamedrew.api.Analyzer;
import ru.usernamedrew.api.Parser;
import ru.usernamedrew.implementation.AnalyzerImpl;
import ru.usernamedrew.implementation.LogParser;

import java.io.IOException;
import java.nio.file.Path;

public class App {
    public static void main( String[] args ) {
        Parser parser = new LogParser();
        Analyzer analyzer = new AnalyzerImpl(Path.of("logs/"), Path.of("transactions_by_users"), parser);
        try {
            analyzer.process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
