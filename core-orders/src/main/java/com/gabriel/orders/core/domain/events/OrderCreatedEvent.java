package com.gabriel.orders.core.domain.events;

import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.events.enums.Operation;

public class OrderCreatedEvent {

    public Operation operation;

    public Order order;

    public OrderCreatedEvent(Operation operation, Order order) {
        this.operation = operation;
        this.order = order;
    }
}
