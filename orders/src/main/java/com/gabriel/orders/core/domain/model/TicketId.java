package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.core.domain.ValueObject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class TicketId extends ValueObject {

    @JsonValue
    @Pattern(regexp = "[0-9a-f]{8}",
        message = "Invalid Ingredient ID format")
    private final String id;

    public TicketId(String id) {
        this.id = id;
        validate();
    }

    @Override
    public void validate() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Invalid ticketId: " + id);
        }
    }
}
