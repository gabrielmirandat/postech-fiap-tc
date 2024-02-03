package com.gabriel.menu.core.domain.port;

import com.gabriel.menu.core.domain.event.ProductCreatedEvent;

public interface ProductPublisher {

    void productCreated(ProductCreatedEvent event);
}
