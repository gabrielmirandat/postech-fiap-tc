package com.gabriel.orders.core.domain.events;

import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.events.enums.OrderEvent;

public record OrderCreatedEvent(Order orderCreated) {

    public String type() {
        return OrderEvent.Type.CREATED.eventType();
    }

    public String source() {
        return OrderEvent.Source.CREATED.eventSource();
    }

    public String subject() {
        return String.format(OrderEvent.Subject.ID.eventSubject(),
            orderCreated.getTicketId());
    }
}
