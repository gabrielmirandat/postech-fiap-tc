package com.gabriel.products.core.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gabriel.products.core.domain.base.ValueObject;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Weight extends ValueObject {

    @DecimalMin(value = "0.1", message = "Weight must be at least 0.1")
    @DecimalMax(value = "10000.0", message = "Weight must be less than 10000.0")
    @NotNull(message = "Weight cannot be null")
    private final double value;

    @JsonCreator
    public Weight(double value) {
        this.value = value;
        validateSelf();
    }
}