package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Price extends ValueObject {

    @DecimalMin(value = "0.1", message = "Price must be at least 0.1")
    @DecimalMax(value = "10000.0", message = "Price must be less than 10000.0")
    @NotNull(message = "Price cannot be null")
    private final double value;

    public Price(double value) {
        this.value = value;
        validateSelf();
    }
}
