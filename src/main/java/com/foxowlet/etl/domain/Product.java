package com.foxowlet.etl.domain;

import lombok.Builder;

@Builder(builderClassName = "Builder")
public record Product(long id, long customerId, String name, int priceCents, int amount) {
    public long totalPriceCents() {
        return (long) priceCents * amount;
    }
}
