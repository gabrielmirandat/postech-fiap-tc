package com.gabriel.products.adapter.driven.messaging;

import com.gabriel.products.adapter.driven.messaging.mapper.CloudEventMapper;
import com.gabriel.products.core.domain.event.IngredientCreatedEvent;
import com.gabriel.products.core.domain.port.IngredientPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
public class IngredientKafkaPublisher implements IngredientPublisher {

    @Inject
    @Channel("ingredient-created")
    Emitter<IngredientCreatedEvent> emitter;

    @Override
    public void ingredientCreated(IngredientCreatedEvent event) {
        this.emitter.send(Message.of(event).addMetadata(CloudEventMapper.fromEvent(event)));
    }
}
