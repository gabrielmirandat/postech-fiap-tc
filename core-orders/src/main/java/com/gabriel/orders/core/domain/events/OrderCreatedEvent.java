package com.gabriel.orders.core.domain.events;

import com.gabriel.orders.core.domain.entities.Order;

public record OrderCreatedEvent(Order order) {
}
