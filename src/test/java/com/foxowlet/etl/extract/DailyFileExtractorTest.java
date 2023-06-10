package com.foxowlet.etl.extract;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static com.foxowlet.etl.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DailyFileExtractorTest {
    private static final Clock clock = makeClock("2023-06-09");

    @Nested
    class WithFormatter {
        @Mock
        private DateTimeFormatter formatter;
        @Captor
        private ArgumentCaptor<LocalDate> dateCaptor;
        private DailyFileExtractor extractor;

        @BeforeEach
        void initExtractor() {
            extractor = new DailyFileExtractor(formatter, clock);
        }

        @Test
        void extract_shouldUseClockCurrentDate() {
            File file = makeTempFile();
            Mockito.when(formatter.format(dateCaptor.capture())).thenReturn(file.getPath());

            extractor.extract();

            assertEquals(LocalDate.now(clock), dateCaptor.getValue());
        }

        @Test
        void extract_shouldReadFileContext_whenFileIsNotEmpty() {
            String[] lines = {"foo", "bar", "buz"};
            File file = makeTempFile(lines);
            Mockito.when(formatter.format(Mockito.any())).thenReturn(file.getPath());

            Stream<String> actual = extractor.extract();

            assertStreamContent(actual, lines);
        }
    }

    @Nested
    class WithPattern {
        @Test
        void extract_shouldUsePattern() {
            File file = makeTempFile("foo");
            DailyFileExtractor extractor = new DailyFileExtractor(escape(file.getPath()), clock);

            Stream<String> actual = extractor.extract();

            assertStreamContent(actual, "foo");
        }

        private static String escape(String value) {
            return String.format("'%s'", value);
        }
    }
}