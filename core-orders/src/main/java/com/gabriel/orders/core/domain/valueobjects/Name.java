package com.gabriel.orders.core.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gabriel.orders.core.domain.base.ValueObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class Name extends ValueObject {

    @NotBlank(message = "Name cannot be null or empty")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private final String value;

    @JsonCreator
    public Name(String value) {
        this.value = value;
        validateSelf();
    }
}
