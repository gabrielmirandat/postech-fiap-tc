package com.gabriel.orders.core.domain.repositories;

import com.gabriel.orders.core.domain.events.OrderEvent;

import java.util.List;

public interface OrderEventStore {

    void save(OrderEvent event);

    List<OrderEvent> getEventsForOrder(String orderId);
}
