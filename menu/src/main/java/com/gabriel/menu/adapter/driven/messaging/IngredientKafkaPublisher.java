package com.gabriel.menu.adapter.driven.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.menu.adapter.driven.messaging.mapper.CloudEventMapper;
import com.gabriel.menu.core.domain.event.IngredientCreatedEvent;
import com.gabriel.menu.core.domain.port.IngredientPublisher;
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

    @Inject
    ObjectMapper objectMapper;

    @Override
    public void ingredientCreated(IngredientCreatedEvent event) throws JsonProcessingException {
        this.emitter.send(Message.of(new String(event.payload(objectMapper), StandardCharsets.UTF_8))
            .addMetadata(CloudEventMapper.fromEvent(event)));
    }
}
