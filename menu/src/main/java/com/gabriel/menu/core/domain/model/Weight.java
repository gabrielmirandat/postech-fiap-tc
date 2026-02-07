package com.gabriel.menu.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class Weight {

    @JsonValue
    @DecimalMin(value = "0.1", message = "Weight must be at least 0.1")
    @DecimalMax(value = "10000.0", message = "Weight must be less than 10000.0")
    @NotNull(message = "Weight cannot be null")
    private final double value;

    @JsonCreator
    public Weight(double value) {
        this.value = value;
        validate();
    }

    private void validate() {
        if (value < 0.1 || value >= 10000.0) {
            throw new IllegalArgumentException("Weight must be between 0.1 and 10000.0");
        }
    }

    public double getValue() {
        return value;
    }
}
