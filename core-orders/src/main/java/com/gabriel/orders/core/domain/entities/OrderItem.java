package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.Entity;
import com.gabriel.orders.core.domain.valueobjects.ids.OrderID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends Entity {

    private final OrderID orderItemID;

    private final Product product;

    private List<Extra> extras;

    public OrderItem(OrderID orderItemID, Product product) {
        this.orderItemID = orderItemID;
        this.product = product;
    }

    public OrderItem(OrderID orderItemID, Product product, List<Extra> extras) {
        this.orderItemID = orderItemID;
        this.product = product;
        this.extras = extras;
    }
}
