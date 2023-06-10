package com.foxowlet.etl.load;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static com.foxowlet.etl.util.TestUtils.*;

class FileLoaderTest {
    private File file;
    private FileLoader loader;

    @BeforeEach
    void setup() {
        file = makeTempFile();
        loader = new FileLoader(file);
    }

    @Test
    void load_shouldWriteContentToFile() {
        String[] lines = {"foo", "bar", "buz"};

        loader.load(Arrays.stream(lines));

        assertFileContent(file, lines);
    }

    @Test
    void load_shouldCreateEmptyFile_whenNoContent() {
        assertTrue(file.delete());
        assertFalse(file.exists());

        loader.load(Stream.empty());

        assertTrue(file.exists());
    }

    @Test
    void load_shouldOverrideFileContent() {
        writeLines(file, "old", "should be replaced");

        String[] lines = {"foo", "bar", "buz"};

        loader.load(Arrays.stream(lines));

        assertFileContent(file, lines);
    }
}