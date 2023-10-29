package com.gabriel.orders.core.domain.valueobjects.ids;

import com.gabriel.orders.core.domain.entities.enums.EntityType;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class OrderItemID extends EntityID {

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
