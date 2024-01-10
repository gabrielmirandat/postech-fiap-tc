package com.gabriel.products.core.domain.ports;

import com.gabriel.products.core.domain.events.ProductCreatedEvent;

public interface ProductPublisher {

    void productCreated(ProductCreatedEvent event);
}
