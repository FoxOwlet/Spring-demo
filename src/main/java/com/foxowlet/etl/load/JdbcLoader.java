package com.foxowlet.etl.load;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Profile("flowerShop")
@Qualifier("summaryLoader")
public class JdbcLoader<T> implements Loader<T> {
    private final DataSource dataSource;
    private final String tableName;
    private final Class<T> entityClass;

    public JdbcLoader(DataSource dataSource, @Value("${table-name}") String tableName, Class<T> entityClass) {
        this.dataSource = dataSource;
        this.tableName = tableName;
        this.entityClass = entityClass;
    }

    @Override
    public void load(Stream<T> data) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(makeQuery())) {
            data.forEach(entity -> addRow(ps, entity));
            ps.executeBatch();
        } catch (SQLException e) {
            throw new IllegalStateException("Can't load entities to the DB table", e);
        }
    }

    private String makeQuery() {
        String[] columnNames = getColumnNames();
        String placeholders = Stream.generate(() -> "?").limit(columnNames.length).collect(Collectors.joining(", "));
        return String.format("INSERT INTO %s(%s) VALUES(%s)",
                tableName,
                String.join(", ", columnNames),
                placeholders);
    }

    private String[] getColumnNames() {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(this::isNotTransient)
                .map(Field::getName)
                .map(this::toSnakeCase)
                .toArray(String[]::new);
    }

    private boolean isNotTransient(Field field) {
        return (field.getModifiers() & Modifier.TRANSIENT) == 0;
    }

    private String toSnakeCase(String name) {
        StringBuilder sb = new StringBuilder();
        for (char ch : name.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                sb.append('_').append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private void addRow(PreparedStatement ps, T entity) {
        Object[] values = getFieldValues(entity);
        try {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            ps.addBatch();
        } catch (SQLException e) {
            throw new IllegalStateException("Can't convert entity to table row", e);
        }
    }

    private Object[] getFieldValues(T entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(this::isNotTransient)
                .map(f -> getFieldValue(f, entity))
                .toArray();
    }

    private Object getFieldValue(Field field, T entity) {
        try {
            field.setAccessible(true);
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Can't get field value for field " + field + " and entity " + entity, e);
        }
    }
}
