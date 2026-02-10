package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabriel.model.OrderItemId;
import com.gabriel.model.Model;

import java.util.Collections;
import java.util.List;

public class OrderItem {

    private final OrderItemId itemID;

    private final Product product;

    private final List<Extra> extras;

    // Private constructor for internal use
    private OrderItem(OrderItemId itemID, Product product, List<Extra> extras) {
        this.itemID = itemID;
        this.product = product;
        this.extras = extras;
    }

    // Factory methods que sempre validam
    public static OrderItem create(Product product) {
        OrderItemId validatedItemID = (OrderItemId) Model.validate(OrderItemId.newBuilder().setValue(generateItemId()).build());
        return new OrderItem(validatedItemID, product, Collections.emptyList());
    }

    public static OrderItem create(Product product, List<Extra> extras) {
        OrderItemId validatedItemID = (OrderItemId) Model.validate(OrderItemId.newBuilder().setValue(generateItemId()).build());
        return new OrderItem(validatedItemID, product, extras);
    }

    private static String generateItemId() {
        return java.util.UUID.randomUUID().toString().substring(0, 8) + "-ORDI-" + 
               java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // Constructor for Jackson deserialization (no validation to avoid duplication)
    @JsonCreator
    public static OrderItem fromJson(@JsonProperty("itemID") String itemIDStr, @JsonProperty("product") Product product,
                                    @JsonProperty("extras") List<Extra> extras) {
        OrderItemId itemID = OrderItemId.newBuilder().setValue(itemIDStr).build();
        return new OrderItem(itemID, product, extras);
    }

    public static OrderItem copy(OrderItemId itemID, Product product, List<Extra> extras) {
        return new OrderItem(itemID, product, extras);
    }

    @JsonIgnore
    public OrderItemId getItemID() {
        return itemID;
    }
    
    @JsonProperty("itemID")
    public String getItemIDString() {
        return itemID.getValue();
    }

    public Product getProduct() {
        return product;
    }

    public List<Extra> getExtras() {
        return extras;
    }
}
