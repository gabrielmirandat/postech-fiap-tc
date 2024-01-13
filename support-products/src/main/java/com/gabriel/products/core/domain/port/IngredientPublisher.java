package com.gabriel.products.core.domain.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.products.core.domain.event.IngredientCreatedEvent;

public interface IngredientPublisher {

    void ingredientCreated(IngredientCreatedEvent event) throws JsonProcessingException;
}
