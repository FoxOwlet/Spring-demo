package com.foxowlet.etl.extract;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileExtractor implements Extractor<String> {
    private final Path filepath;

    public FileExtractor(String filename) {
        this(new File(filename));
    }

    public FileExtractor(File file) {
        this(file.toPath());
    }

    public FileExtractor(Path filepath) {
        this.filepath = filepath;
    }

    @Override
    public Stream<String> extract() {
        try {
            return Files.lines(filepath);
        } catch (IOException e) {
            throw new IllegalStateException("Can't read file " + filepath, e);
        }
    }
}
