package com.gabriel.common.core.domain.model;

import com.gabriel.common.core.domain.base.ValueObject;
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
