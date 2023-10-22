package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.entities.enums.EntityType;

public class OrderID extends EntityID {

    public OrderID() {
        super(EntityType.ORDER);
    }

    public OrderID(String id) {
        super(id);
        this.validate(id);
    }

    public void validate(String id) {
        String[] intrinsics = id.split("-");
        assert intrinsics[0].equals(EntityType.ORDER.getDefaultRepr());

    }
}
