package com.gabriel.orders.core.domain.events;

import com.gabriel.orders.core.domain.base.DomainEvent;
import com.gabriel.orders.core.domain.entities.Order;
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
}
