package com.gabriel.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.domain.ValueObject;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Price extends ValueObject {

    @JsonValue
    @DecimalMin(value = "0.1", message = "Price must be at least 0.1")
    @DecimalMax(value = "10000.0", message = "Price must be less than 10000.0")
    @NotNull(message = "Price cannot be null")
    private final double value;

    @JsonCreator
    public Price(double value) {
        this.value = value;
        validateSelf();
    }
}
