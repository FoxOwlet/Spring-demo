package com.foxowlet.etl.format;

import com.foxowlet.etl.domain.CustomerSummary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Profile("fruitShop")
public class CustomerSummaryCSVFormatter implements Formatter<CustomerSummary> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    @Override
    public String format(CustomerSummary entity) {
        return String.join(",",
                formatter.format(entity.date()),
                Long.toString(entity.customerId()),
                Long.toString(entity.uniqueProducts()),
                Long.toString(entity.totalProducts()),
                Long.toString(entity.totalPrice()));
    }
}
