package com.gabriel.orders.core.application.command;

import com.gabriel.orders.core.domain.model.OrderStatus;

public record ProcessOrderCommand(String ticketId, OrderStatus status) {
}
