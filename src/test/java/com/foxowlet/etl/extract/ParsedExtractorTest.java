package com.foxowlet.etl.extract;

import com.foxowlet.etl.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static com.foxowlet.etl.util.TestUtils.assertStreamContent;

@ExtendWith(MockitoExtension.class)
class ParsedExtractorTest {
    @Mock
    private Extractor<String> rawExtractor;
    @Mock
    private Parser<Integer> parser;
    @InjectMocks
    private ParsedExtractor<Integer> extractor;

    @Test
    void extract_shouldParseRawContent() {
        initRawExtractor("a", "b", "c");
        initParser(Map.of("a", 1, "b", 2, "c", 3));

        Stream<Integer> actual = extractor.extract();

        assertStreamContent(actual, 1, 2, 3);
    }

    private void initRawExtractor(String... content) {
        Mockito.when(rawExtractor.extract()).thenReturn(Arrays.stream(content));
    }

    private void initParser(Map<String, Integer> spec) {
        for (Map.Entry<String, Integer> entry : spec.entrySet()) {
            Mockito.when(parser.parse(entry.getKey())).thenReturn(entry.getValue());
        }
    }
}