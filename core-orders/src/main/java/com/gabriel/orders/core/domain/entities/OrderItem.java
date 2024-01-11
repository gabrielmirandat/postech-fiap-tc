package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.Entity;
import com.gabriel.orders.core.domain.valueobjects.Extra;
import com.gabriel.orders.core.domain.valueobjects.Product;
import com.gabriel.orders.core.domain.valueobjects.ids.OrderItemID;
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
}
