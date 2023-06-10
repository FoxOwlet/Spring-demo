package com.foxowlet.etl.extract;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.foxowlet.etl.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class FileExtractorTest {
    @Test
    void extract_shouldReturnEmptyStream_whenFileIsEmpty() {
        FileExtractor extractor = new FileExtractor(makeTempFile());

        Stream<String> actual = extractor.extract();

        assertFalse(actual.findAny().isPresent());
    }

    @Test
    void extract_shouldReadFileContent_whenFileIsNotEmpty() {
        String[] lines = {"foo", "bar", "buz"};
        FileExtractor extractor = new FileExtractor(makeTempFile(lines));

        Stream<String> actual = extractor.extract();

        assertStreamContent(actual, lines);
    }

    @Test
    void extract_shouldThrowException_whenFileIsMissing() {
        FileExtractor extractor = new FileExtractor("foo.txt");

        assertThrows(IllegalStateException.class, extractor::extract);
    }
}