package com.gabriel.orders.core.application.command;

import com.gabriel.orders.core.domain.model.TicketId;

public record CancelOrderCommand(TicketId ticketId) {
}
