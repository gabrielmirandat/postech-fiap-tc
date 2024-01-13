package com.gabriel.common.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.common.core.domain.base.ValueObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class Name extends ValueObject {

    @JsonValue
    @NotBlank(message = "Name cannot be null or empty")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private final String value;

    @JsonCreator
    public Name(String value) {
        this.value = value;
        validateSelf();
    }
}
