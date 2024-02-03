package com.gabriel.orders.core.domain.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.DomainEvent;
import com.gabriel.orders.core.domain.model.Order;
import lombok.Getter;

@Getter
public class OrderCreatedEvent implements DomainEvent {

    Order orderCreated;

    public OrderCreatedEvent(Order orderCreated) {
        this.orderCreated = orderCreated;
    }

    @Override
    public String source() {
        return "post/orders";
    }

    @Override
    public String subject() {
        return String.format("id/%s", orderCreated.getOrderId().getId());
    }

    @Override
    public String type() {
        return "postech.orders.v1.order.created";
    }

    @Override
    public byte[] payload(ObjectMapper serializer) throws JsonProcessingException {
        if (orderCreated == null) {
            throw new IllegalStateException("Order is null");
        }
        return serializer.writeValueAsBytes(orderCreated);
    }
}
