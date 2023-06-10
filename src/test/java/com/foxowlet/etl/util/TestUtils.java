package com.foxowlet.etl.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public final class TestUtils {
    private TestUtils() {}

    public static <T> void assertStreamContent(Stream<T> stream, T... content) {
        Assertions.assertArrayEquals(content, stream.toArray());
    }

    @SneakyThrows
    public static void assertFileContent(File file, String... content) {
        Assertions.assertArrayEquals(content, Files.readAllLines(file.toPath()).toArray());
    }

    @SneakyThrows
    public static void assertTableContent(Connection connection, String tableName, Map<String, Object>... rows) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tableName);
             ResultSet rs = ps.executeQuery()) {
            for (Map<String, Object> row : rows) {
                Assertions.assertTrue(rs.next(), "Row not found for " + row);
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    Assertions.assertEquals(entry.getValue(), rs.getObject(entry.getKey()));
                }
            }
        }
    }

    @SneakyThrows
    public static File makeTempFile(String... content) {
        File file = File.createTempFile("testfile", "txt");
        file.deleteOnExit();
        if (content.length != 0) {
            writeLines(file, content);
        }
        return file;
    }

    @SneakyThrows
    public static void writeLines(File file, String... content) {
        Files.write(file.toPath(), Arrays.asList(content), StandardOpenOption.WRITE);
    }

    public static Clock makeClock(String isoDate) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate date = LocalDate.parse(isoDate);
        Instant instant = date.atStartOfDay(zone).toInstant();
        return Clock.fixed(instant, zone);
    }
}
