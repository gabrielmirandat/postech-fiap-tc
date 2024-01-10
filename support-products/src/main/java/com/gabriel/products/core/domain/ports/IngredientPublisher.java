package com.gabriel.products.core.domain.ports;

import com.gabriel.products.core.domain.events.IngredientCreatedEvent;

public interface IngredientPublisher {

    void ingredientCreated(IngredientCreatedEvent event);
}
