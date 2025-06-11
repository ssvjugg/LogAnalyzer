package ru.usernamedrew.api;

import java.io.IOException;
import java.nio.file.Path;

public interface Analyzer {

    /**
     * This function represents main business logic of Analyzer.
     * It'll read files from input directory, process them, and then it will make finale files for each user from log files.
     * @throws IOException if an I/O error occurs opening the file
     */
    void process() throws IOException;

    /**
     * Getter for directory that will contain final files.
     * @return Output path
     */
    Path getOutputPath();

    /**
     * Getter for directory that will contain log files.
     * @return Input path
     */
    Path getInputPath();
}
