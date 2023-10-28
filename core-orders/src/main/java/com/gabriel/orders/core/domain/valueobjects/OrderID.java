package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.entities.enums.EntityType;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class OrderID extends EntityID {

    @Pattern(regexp = "[0-9a-f]{8}-ORDR-\\d{4}-\\d{2}-\\d{2}",
            message = "Invalid Order ID format")
    private final String id;

    public OrderID() {
        this.id = generate(EntityType.ORDER);
        validateSelf();
    }

    public OrderID(String id) {
        this.id = id;
        validateSelf();
    }
}
