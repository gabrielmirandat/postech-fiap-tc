package com.gabriel.orders.core.domain.model;

import com.gabriel.core.domain.Entity;
import com.gabriel.core.domain.model.id.OrderItemID;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class OrderItem extends Entity {

    private final OrderItemID itemID;

    private final Product product;

    private final List<Extra> extras;

    public OrderItem(Product product) {
        this.product = product;
        this.extras = Collections.emptyList();

        this.itemID = new OrderItemID();
    }

    public OrderItem(Product product, List<Extra> extras) {
        this.product = product;
        this.extras = extras;

        this.itemID = new OrderItemID();
    }

    private OrderItem(OrderItemID itemID, Product product, List<Extra> extras) {
        this.itemID = itemID;
        this.product = product;
        this.extras = extras;
    }

    public static OrderItem copy(OrderItemID itemID, Product product, List<Extra> extras) {
        return new OrderItem(itemID, product, extras);
    }
}
