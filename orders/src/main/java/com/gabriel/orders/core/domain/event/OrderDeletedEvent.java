package com.gabriel.orders.core.domain.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.DomainEvent;
import lombok.Getter;

@Getter
public class OrderDeletedEvent implements DomainEvent {

    private final String ticketId;

    public OrderDeletedEvent(String ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public String source() {
        return "delete/orders";
    }

    @Override
    public String subject() {
        return String.format("id/%s", ticketId);
    }

    @Override
    public String type() {
        return "postech.orders.v1.order.deleted";
    }

    @Override
    public byte[] payload(ObjectMapper serializer) throws JsonProcessingException {
        if (ticketId == null) {
            throw new IllegalStateException("Order is null");
        }
        return serializer.writeValueAsBytes(ticketId);
    }
}
