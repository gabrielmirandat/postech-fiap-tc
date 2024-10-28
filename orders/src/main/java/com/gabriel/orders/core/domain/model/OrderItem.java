package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabriel.core.domain.Entity;
import com.gabriel.core.domain.model.id.OrderItemID;

import java.util.Collections;
import java.util.List;

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

    /**
     * Constructor for Jackson deserialization.
     */
    @JsonCreator
    OrderItem(@JsonProperty("itemID") OrderItemID itemID, @JsonProperty("product") Product product,
              @JsonProperty("extras") List<Extra> extras) {
        this.itemID = itemID;
        this.product = product;
        this.extras = extras;
    }

    public static OrderItem copy(OrderItemID itemID, Product product, List<Extra> extras) {
        return new OrderItem(itemID, product, extras);
    }

    public OrderItemID getItemID() {
        return itemID;
    }

    public Product getProduct() {
        return product;
    }

    public List<Extra> getExtras() {
        return extras;
    }
}
