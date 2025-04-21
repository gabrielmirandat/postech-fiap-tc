package com.gabriel.orders.core.domain.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderDeletedEvent {

    private final String ticketId;

    public OrderDeletedEvent(String ticketId) {
        this.ticketId = ticketId;
    }

    public String source() {
        return "delete/orders";
    }

    public String subject() {
        return String.format("id/%s", ticketId);
    }

    public String type() {
        return "postech.orders.v1.order.deleted";
    }

    public byte[] payload(ObjectMapper serializer) throws JsonProcessingException {
        if (ticketId == null) {
            throw new IllegalStateException("Order is null");
        }
        return serializer.writeValueAsBytes(ticketId);
    }

    public String getTicketId() {
        return ticketId;
    }
}
