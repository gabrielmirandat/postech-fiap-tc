package com.gabriel.orders.core.domain.valueobjects;

public record Price(float value) {

    public Price {
        this.validate(value);
    }

    private void validate(float value) {
        assert value > 0.0;
    }
}
