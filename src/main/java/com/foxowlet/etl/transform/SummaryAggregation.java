package com.foxowlet.etl.transform;

import com.foxowlet.etl.domain.CustomerSummary;
import com.foxowlet.etl.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SummaryAggregation implements Transformation<Product, CustomerSummary> {
    private final Clock clock;

    @Autowired
    public SummaryAggregation(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Stream<CustomerSummary> transform(Stream<Product> data) {
        return data.collect(Collectors.groupingBy(Product::customerId))
                .entrySet()
                .stream()
                .map(this::aggregate);
    }

    private CustomerSummary aggregate(Map.Entry<Long, List<Product>> products) {
        Set<Long> uniqueIds = new HashSet<>();
        long totalPrice = 0;
        int totalProducts = 0;
        for (Product product : products.getValue()) {
            uniqueIds.add(product.id());
            totalProducts += product.amount();
            totalPrice += product.totalPriceCents();
        }
        return new CustomerSummary(
                LocalDate.now(clock),
                products.getKey(),
                uniqueIds.size(),
                totalProducts,
                totalPrice);
    }
}
