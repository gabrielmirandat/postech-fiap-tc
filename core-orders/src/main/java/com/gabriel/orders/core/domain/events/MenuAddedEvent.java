package com.gabriel.orders.core.domain.events;

import com.gabriel.orders.core.domain.entities.Product;

public record MenuAddedEvent(Product productAdded) {

    private static final String EVENT_TYPE = "postech.menu.gateway.v1.product.created";

    private static final String SOURCE_PATTERN = "/products/%s";

    private static final String SUBJECT = "new-menu-product";

    public String type() {
        return EVENT_TYPE;
    }

    public String source() {
        return String.format(SOURCE_PATTERN, productAdded.getProductID().toString());
    }

    public String subject() {
        return SUBJECT;
    }
}
