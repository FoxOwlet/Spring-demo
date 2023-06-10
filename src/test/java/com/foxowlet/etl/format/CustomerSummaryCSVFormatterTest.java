package com.foxowlet.etl.format;

import com.foxowlet.etl.domain.CustomerSummary;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerSummaryCSVFormatterTest {
    private CustomerSummaryCSVFormatter formatter = new CustomerSummaryCSVFormatter();

    @Test
    void format_shouldReturnCSVRow() {
        CustomerSummary entity = new CustomerSummary(
                LocalDate.parse("2023-06-09"),
                1, 2, 3, 4);

        String actual = formatter.format(entity);

        assertEquals("2023-06-09,1,2,3,4", actual);
    }
}