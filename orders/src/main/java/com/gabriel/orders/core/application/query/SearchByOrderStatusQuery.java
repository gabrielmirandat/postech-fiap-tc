package com.gabriel.orders.core.application.query;

import com.gabriel.orders.core.domain.model.OrderStatus;

public record SearchByOrderStatusQuery(OrderStatus status) {
}
