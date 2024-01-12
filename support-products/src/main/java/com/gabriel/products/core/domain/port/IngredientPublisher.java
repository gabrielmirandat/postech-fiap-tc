package com.gabriel.products.core.domain.port;

import com.gabriel.products.core.domain.event.IngredientCreatedEvent;

public interface IngredientPublisher {

    void ingredientCreated(IngredientCreatedEvent event);
}
