package com.gabriel.core.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.core.domain.ValueObject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class Quantity extends ValueObject {

    @JsonValue
    @Min(1)
    @Max(10)
    private final int size;

    public Quantity(int size) {
        this.size = size;
        validate();
    }
}
