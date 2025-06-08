package ru.usernamedrew.api;

import java.io.IOException;
import java.nio.file.Path;

public interface Analyzer {

    void process() throws IOException;

    void writeToFile(Path path);
}
