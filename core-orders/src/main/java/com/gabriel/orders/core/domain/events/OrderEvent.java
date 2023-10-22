package com.gabriel.orders.core.domain.events;

import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.events.enums.Operation;

public class OrderEvent {

    public Operation operation;

    public Order order;

    public OrderEvent(Operation operation, Order order) {
        this.operation = operation;
        this.order = order;
    }
}
