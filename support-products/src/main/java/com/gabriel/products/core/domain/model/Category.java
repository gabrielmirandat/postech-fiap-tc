package com.gabriel.products.core.domain.model;

public enum Category {

    BURGER("burger"),

    ACCOMPANIMENT("accompaniment"),

    DESSERT("dessert"),

    DRINK("drink");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
