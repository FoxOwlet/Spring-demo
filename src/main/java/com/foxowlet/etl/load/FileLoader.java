package com.foxowlet.etl.load;

import java.io.*;
import java.util.stream.Stream;

public class FileLoader implements Loader<String> {
    private final File outFile;

    public FileLoader(String filename) {
        this(new File(filename));
    }

    public FileLoader(File outFile) {
        this.outFile = outFile;
    }

    @Override
    public void load(Stream<String> data) {
        try (FileWriter fileWriter = new FileWriter(outFile);
             PrintWriter writer = new PrintWriter(fileWriter)) {
            data.forEach(writer::println);
        } catch (IOException e) {
            throw new IllegalStateException("Can't write file " + outFile.getPath(), e);
        }
    }
}
