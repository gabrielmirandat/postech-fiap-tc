package com.gabriel.orders.core.domain.port;

import com.gabriel.orders.core.domain.event.OrderCreatedEvent;

public interface OrderPublisher {

    void orderCreated(OrderCreatedEvent event);
}
