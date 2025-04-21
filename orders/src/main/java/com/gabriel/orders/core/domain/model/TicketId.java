package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Pattern;

public class TicketId {

    @JsonValue
    @Pattern(regexp = "[0-9a-f]{8}",
        message = "Invalid Ticket Id format")
    private final String id;

    public TicketId(String id) {
        this.id = id;
        validate();
    }

    public void validate() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Invalid ticketId: " + id);
        }
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketId ticketId = (TicketId) o;
        return id.equals(ticketId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }
}
