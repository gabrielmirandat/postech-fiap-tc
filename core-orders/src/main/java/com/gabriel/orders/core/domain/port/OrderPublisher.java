package com.gabriel.orders.core.domain.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.orders.core.domain.event.OrderCreatedEvent;

public interface OrderPublisher {

    void orderCreated(OrderCreatedEvent event) throws JsonProcessingException;
}
