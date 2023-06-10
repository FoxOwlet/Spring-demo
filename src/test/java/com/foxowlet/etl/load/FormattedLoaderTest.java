package com.foxowlet.etl.load;

import com.foxowlet.etl.format.Formatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.stream.Stream;

import static com.foxowlet.etl.util.TestUtils.*;

@ExtendWith(MockitoExtension.class)
class FormattedLoaderTest {
    @Mock
    private Loader<String> mockLoader;
    @Mock
    private Formatter<Integer> formatter;
    @InjectMocks
    private FormattedLoader<Integer> loader;
    @Captor
    private ArgumentCaptor<Stream<String>> captor;

    @Test
    void load_shouldFormatContent() {
        initLoader();
        initFormatter(Map.of(1, "a", 2, "b", 3, "c"));

        loader.load(Stream.of(1, 2, 2, 3));

        assertStreamContent(captor.getValue(), "a", "b", "b", "c");
    }

    private void initLoader() {
        Mockito.doNothing().when(mockLoader).load(captor.capture());
    }

    private void initFormatter(Map<Integer, String> spec) {
        for (Map.Entry<Integer, String> entry : spec.entrySet()) {
            Mockito.when(formatter.format(entry.getKey())).thenReturn(entry.getValue());
        }
    }
}