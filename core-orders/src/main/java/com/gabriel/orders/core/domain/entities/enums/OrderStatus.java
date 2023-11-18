package com.gabriel.orders.core.domain.entities.enums;

public enum OrderStatus {
    CREATED("created"),

    PREPARATION("preparation"),

    PACKAGING("packaging"),

    PICKUP("pickup"),

    DELIVERY("delivery"),

    COMPLETED("completed");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
