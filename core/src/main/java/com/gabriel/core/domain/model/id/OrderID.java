package com.gabriel.core.domain.model.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.core.domain.EntityType;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class OrderID extends EntityID {

    @JsonValue
    @Pattern(regexp = "[0-9a-f]{8}-ORDR-\\d{4}-\\d{2}-\\d{2}",
        message = "Invalid Order ID format")
    private final String id;

    public OrderID() {
        this.id = generate(EntityType.ORDER);
        validate();
    }

    public OrderID(String id) {
        this.id = id;
        validate();
    }
}
