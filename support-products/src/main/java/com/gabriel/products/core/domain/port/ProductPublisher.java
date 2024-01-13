package com.gabriel.products.core.domain.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.products.core.domain.event.ProductCreatedEvent;

public interface ProductPublisher {

    void productCreated(ProductCreatedEvent event) throws JsonProcessingException;
}
