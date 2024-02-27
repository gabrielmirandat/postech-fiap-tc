package com.gabriel.orders.core.application.command;

import com.gabriel.orders.core.domain.model.OrderStatus;
import com.gabriel.orders.core.domain.model.TicketId;

public record ProcessOrderCommand(TicketId ticketId, OrderStatus status) {
}
