package com.gabriel.orders.core.domain.ports;

import com.gabriel.orders.core.domain.events.OrderCreatedEvent;

public interface OrderPublisher {

    void orderCreated(OrderCreatedEvent event);
}
