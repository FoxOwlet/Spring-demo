package com.foxowlet.etl.transform;

import com.foxowlet.etl.domain.CustomerSummary;
import com.foxowlet.etl.domain.Product;
import com.foxowlet.etl.util.TestUtils;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Stream;

import static com.foxowlet.etl.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class SummaryAggregationTest {
    private Clock clock = makeClock("2023-06-09");
    private SummaryAggregation aggregation = new SummaryAggregation(clock);

    @Test
    void transform_shouldAggregateProductsInfoByCustomerId() {
        CustomerSummary[] expected = {
                new CustomerSummary(LocalDate.now(clock), 1, 2, 4, 50),
                new CustomerSummary(LocalDate.now(clock), 2, 1, 1, 15),
        };

        Stream<CustomerSummary> actual = aggregation.transform(Stream.of(
                new Product(1, 1, "foo", 10, 1),
                new Product(1, 1, "foo", 10, 2),
                new Product(2, 2, "bar", 15, 1),
                new Product(2, 1, "bar", 20, 1)));

        assertStreamContent(actual.sorted(Comparator.comparing(CustomerSummary::customerId)), expected);
    }
}