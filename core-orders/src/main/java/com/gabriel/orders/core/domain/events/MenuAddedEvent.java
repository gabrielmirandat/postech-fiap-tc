package com.gabriel.orders.core.domain.events;

import com.gabriel.orders.core.domain.entities.Product;
import com.gabriel.orders.core.domain.events.enums.MenuEvent;

public record MenuAddedEvent(Product productAdded) {

    public String type() {
        return MenuEvent.Type.CREATED.eventType();
    }

    public String source() {
        return MenuEvent.Source.CREATED.eventSource();
    }

    public String subject() {
        return String.format(MenuEvent.Subject.ID.eventSubject(),
                productAdded.getProductID().toString());
    }
}
