package com.foxowlet.etl.format;

import com.foxowlet.etl.domain.CustomerSummary;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerSummaryJSONFormatterTest {
    private CustomerSummaryJSONFormatter formatter = new CustomerSummaryJSONFormatter();

    @Test
    void format_shouldReturnJSONObject() {
        CustomerSummary entity = new CustomerSummary(
                LocalDate.parse("2023-06-09"),
                1, 2, 3, 4);

        String actual = formatter.format(entity);

        assertEquals("""
                {"date": "2023-06-09", "customerId": 1, "unique": 2, "total": 3, "price": 4}""",
                actual);
    }
}