package com.foxowlet.etl.format;

import com.foxowlet.etl.domain.CustomerSummary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Profile("flowerShop")
public class CustomerSummaryJSONFormatter implements Formatter<CustomerSummary> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
    private static final Map<String, Function<CustomerSummary, Object>> attributesMap = new LinkedHashMap<>();

    static {
        attributesMap.put("date", e -> formatter.format(e.date()));
        attributesMap.put("customerId", CustomerSummary::customerId);
        attributesMap.put("unique", CustomerSummary::uniqueProducts);
        attributesMap.put("total", CustomerSummary::totalProducts);
        attributesMap.put("price", CustomerSummary::totalPrice);
    }

    @Override
    public String format(CustomerSummary entity) {
        return attributesMap.entrySet()
                .stream()
                .map(e -> format(e, entity))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    private String format(Map.Entry<String, Function<CustomerSummary, Object>> spec, CustomerSummary entity) {
        return String.format("%s: %s",
                escape(spec.getKey()),
                maybeEscape(spec.getValue().apply(entity)));
    }

    private Object maybeEscape(Object value) {
        if (value instanceof String s) {
            return escape(s);
        }
        return value;
    }


    private static String escape(String value) {
        return '"' + value + '"';
    }
}
