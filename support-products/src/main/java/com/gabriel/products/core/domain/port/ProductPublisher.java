package com.gabriel.products.core.domain.port;

import com.gabriel.products.core.domain.event.ProductCreatedEvent;

public interface ProductPublisher {

    void productCreated(ProductCreatedEvent event);
}
