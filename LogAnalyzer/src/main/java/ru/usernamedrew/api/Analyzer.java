package ru.usernamedrew.api;

import java.nio.file.Path;

public interface Analyzer {

    void processLogLines();

    void writeToFile(Path path);
}
