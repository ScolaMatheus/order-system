package com.microservico.customer.util;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrigemCancelamento {
    CUSTOMER_SERVICE("customer-service"),
    RESTAURANT_SERVICE("restaurant-service");

    private final String value;

    OrigemCancelamento(String value) {
        this.value = value;
    }

    @JsonCreator
    public static OrigemCancelamento fromValue(String value) {
        for (OrigemCancelamento origem : OrigemCancelamento.values()) {
            if (origem.value.equalsIgnoreCase(value)) {
                return origem;
            }
        }
        throw new IllegalArgumentException("Origem inválida: " + value);
    }
}