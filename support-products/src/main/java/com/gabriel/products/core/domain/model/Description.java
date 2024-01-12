package com.gabriel.products.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gabriel.common.core.domain.base.ValueObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class Description extends ValueObject {

    @NotBlank(message = "Description cannot be null or empty")
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private final String value;

    @JsonCreator
    public Description(String value) {
        this.value = value;
        validateSelf();
    }
}
