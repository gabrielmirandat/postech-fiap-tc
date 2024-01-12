package com.gabriel.products.adapter.driven.messaging;

import com.gabriel.products.adapter.driven.messaging.mapper.CloudEventMapper;
import com.gabriel.products.core.domain.event.ProductCreatedEvent;
import com.gabriel.products.core.domain.port.ProductPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
public class ProductKafkaPublisher implements ProductPublisher {

    @Inject
    @Channel("product-created")
    Emitter<ProductCreatedEvent> emitter;

    @Override
    public void productCreated(ProductCreatedEvent event) {
        this.emitter.send(Message.of(event).addMetadata(CloudEventMapper.fromEvent(event)));
    }
}
