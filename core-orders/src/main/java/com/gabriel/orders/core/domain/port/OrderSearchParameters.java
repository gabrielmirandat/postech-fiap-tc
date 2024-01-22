package com.gabriel.orders.core.domain.port;

import com.gabriel.orders.core.domain.model.OrderStatus;

public record OrderSearchParameters(OrderStatus status) {
}
