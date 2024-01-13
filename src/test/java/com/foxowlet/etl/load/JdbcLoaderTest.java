package com.foxowlet.etl.load;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.sql.*;
import java.util.Map;
import java.util.stream.Stream;

import static com.foxowlet.etl.util.TestUtils.assertTableContent;

@ExtendWith(MockitoExtension.class)
class JdbcLoaderTest {
    private record TestData(int id, long testValue, String name) {}

    private static final String TABLE_DDL =
            "CREATE TABLE test_data(id INT, test_value BIGINT, name VARCHAR2(100))";
    @Mock
    private DataSource dataSource;
    private JdbcLoader<TestData> loader;

    @BeforeEach
    void initDataBase() throws SQLException {
        Mockito.when(dataSource.getConnection()).thenAnswer(__ -> getConnection());
        loader = new JdbcLoader<>(new JdbcTemplate(dataSource), "test_data", TestData.class);
        createTable();
    }

    @Test
    void load_shouldInsertDataToDB() {
        loader.load(Stream.of(
                new TestData(1, 2, "foo"),
                new TestData(2, 42, "bar")));

        assertTableContent(getConnection(), "test_data",
                Map.of("id", 1, "test_value", 2L, "name", "foo"),
                Map.of("id", 2, "test_value", 42L, "name", "bar"));
    }

    @SneakyThrows
    private Connection getConnection() {
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    }

    private void createTable() throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(TABLE_DDL);
        }
    }
}