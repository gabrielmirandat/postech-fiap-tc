package com.gabriel.orders.core.domain.model;

public enum OrderStatus {
    CREATED("created"),

    PREPARATION("preparation"),

    PACKAGING("packaging"),

    PICKUP("pickup"),

    DELIVERY("delivery"),

    COMPLETED("completed"),

    CANCELED("canceled");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
