package com.foxowlet.etl.parser;

import com.foxowlet.etl.domain.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
public class ProductCSVParser implements Parser<Product> {
    private static final String ENTITY = "product";
    private static final String NULL_INPUT_REASON = "input is null";
    private static final String COLUMN_COUNT_REASON = "invalid column count";
    private static final String DEFAULT_DELIMITER = ",";
    private static final ArrayList<BiFunction<Product.Builder, String, Product.Builder>> conversions = new ArrayList<>();

    static {
        conversions.add(combine(Long::parseLong, Product.Builder::id));
        conversions.add(combine(Long::parseLong, Product.Builder::customerId));
        conversions.add(Product.Builder::name);
        conversions.add(combine(Integer::parseInt, Product.Builder::priceCents));
        conversions.add(combine(Integer::parseInt, Product.Builder::amount));
        conversions.trimToSize();
    }

    private final String delimiter;

    public ProductCSVParser() {
        this(DEFAULT_DELIMITER);
    }

    public ProductCSVParser(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public Product parse(String input) {
        if (input == null) {
            throw new ParseException(ENTITY, NULL_INPUT_REASON, input);
        }
        int columnsCount = conversions.size();
        String[] parts = input.split(delimiter, columnsCount);
        if (parts.length != columnsCount) {
            throw new ParseException(ENTITY, COLUMN_COUNT_REASON, input);
        }
        return tryBuildProduct(input, parts);
    }

    private Product tryBuildProduct(String input, String[] parts) {
        try {
            return buildProduct(parts);
        } catch (NumberFormatException e) {
            throw new ParseException(ENTITY, input, e);
        }
    }

    private Product buildProduct(String[] parts) {
        Product.Builder builder = Product.builder();
        int partIdx = 0;
        for (BiFunction<Product.Builder, String, Product.Builder> conversion : conversions) {
            conversion.apply(builder, parts[partIdx++]);
        }
        return builder.build();
    }

    private static <T> BiFunction<Product.Builder, String, Product.Builder> combine(
            Function<String, T> parseFn,
            BiFunction<Product.Builder, T, Product.Builder> builderFn) {
        return (builder, s) -> builderFn.apply(builder, parseFn.apply(s));
    }
}
