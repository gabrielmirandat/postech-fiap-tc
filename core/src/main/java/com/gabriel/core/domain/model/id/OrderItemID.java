package com.gabriel.core.domain.model.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.core.domain.EntityType;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class OrderItemID extends EntityID {

    @JsonValue
    @Pattern(regexp = "[0-9a-f]{8}-ORDI-\\d{4}-\\d{2}-\\d{2}",
        message = "Invalid Order Item ID format")
    private final String id;

    public OrderItemID() {
        this.id = generate(EntityType.ORDER_ITEM);
        validateSelf();
    }

    public OrderItemID(String id) {
        this.id = id;
        validateSelf();
    }
}
