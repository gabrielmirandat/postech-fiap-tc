package com.gabriel.orders.core.domain.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.orders.core.domain.model.Order;

public class OrderCreatedEvent {

    Order orderCreated;

    public OrderCreatedEvent(Order orderCreated) {
        this.orderCreated = orderCreated;
    }

    public String source() {
        return "post/orders";
    }

    public String subject() {
        return String.format("id/%s", orderCreated.getOrderId().getId());
    }

    public String type() {
        return "postech.orders.v1.order.created";
    }

    public byte[] payload(ObjectMapper serializer) throws JsonProcessingException {
        if (orderCreated == null) {
            throw new IllegalStateException("Order is null");
        }
        return serializer.writeValueAsBytes(orderCreated);
    }

    public Order getOrderCreated() {
        return orderCreated;
    }
}
