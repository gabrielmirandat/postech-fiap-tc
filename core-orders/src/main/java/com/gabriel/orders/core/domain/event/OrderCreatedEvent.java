package com.gabriel.orders.core.domain.event;

import com.gabriel.common.core.domain.base.DomainEvent;
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
}
