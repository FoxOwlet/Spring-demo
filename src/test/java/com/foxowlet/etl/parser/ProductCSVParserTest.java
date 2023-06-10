package com.foxowlet.etl.parser;

import com.foxowlet.etl.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ProductCSVParserTest {
    private ProductCSVParser parser = new ProductCSVParser();

    @Test
    void parse_shouldReturnCorrectObject() {
        String input = "1,2,foo,3,4";
        Product expected = new Product(1, 2, "foo", 3, 4);

        Product actual = parser.parse(input);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1,2,3,4", // not enough columns
            "1,2,3,4,5,6", // too many columns
            "1,2,3,4,5,", // too many columns
            ",2,3,4,5,6", // too many columns
            ",2,3,4,5,", // too many columns
            "a,2,3,4,5", // invalid id
            ",2,3,4,5", // invalid id
            "1,b,3,4,5", // invalid customer id
            "1,2,3,c,5", // invalid price
            "1,2,3,4,d", // invalid amount
            ",,,",
            ",,,,",
            ",,,,,"
    })
    @NullAndEmptySource
    void parse_shouldThrowParseException_whenInvalidInput(String input) {
        assertThrows(ParseException.class, () -> parser.parse(input));
    }
}