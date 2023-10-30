package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.ValueObject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class Quantity extends ValueObject {

    @Min(1)
    @Max(10)
    private final int size;

    public Quantity(int size) {
        this.size = size;
        validateSelf();
    }
}
