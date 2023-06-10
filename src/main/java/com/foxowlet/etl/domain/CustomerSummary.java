package com.foxowlet.etl.domain;

import java.time.LocalDate;

public record CustomerSummary(LocalDate date, long customerId, int uniqueProducts, int totalProducts, long totalPrice) {
}
