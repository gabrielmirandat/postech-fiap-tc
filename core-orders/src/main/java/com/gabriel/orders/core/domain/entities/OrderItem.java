package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.Entity;
import com.gabriel.orders.core.domain.valueobjects.ids.OrderID;
import com.gabriel.orders.core.domain.valueobjects.ids.OrderItemID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends Entity {

    private OrderItemID itemID;

    private final Product product;

    private List<Extra> extras;

    public OrderItem(Product product) {
        this.product = product;

        this.itemID = new OrderItemID();
    }

    public OrderItem(Product product, List<Extra> extras) {
        this.product = product;
        this.extras = extras;

        this.itemID = new OrderItemID();
    }
}
