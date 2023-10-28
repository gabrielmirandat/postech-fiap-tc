package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Price extends ValueObject {

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private final Double value;

    public Price(Double value) {
        this.value = value;
        validateSelf();
    }
}
