package com.gabriel.products.adapter.driven.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.products.adapter.driven.messaging.mapper.CloudEventMapper;
import com.gabriel.products.core.domain.event.IngredientCreatedEvent;
import com.gabriel.products.core.domain.port.IngredientPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class IngredientKafkaPublisher implements IngredientPublisher {

    @Inject
    @Channel("ingredient-created")
    Emitter<String> emitter;

    @Override
    public void ingredientCreated(IngredientCreatedEvent event) throws JsonProcessingException {
        this.emitter.send(Message.of(new String(event.payload(), StandardCharsets.UTF_8))
            .addMetadata(CloudEventMapper.fromEvent(event)));
    }
}
