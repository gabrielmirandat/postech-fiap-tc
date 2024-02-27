package com.gabriel.orders.core.application.query;

import com.gabriel.orders.core.domain.model.TicketId;

public record GetByTicketOrderQuery(TicketId ticket) {
}
