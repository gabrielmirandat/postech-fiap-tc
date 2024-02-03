package com.gabriel.menu.core.domain.port;

import com.gabriel.menu.core.domain.event.IngredientCreatedEvent;

public interface IngredientPublisher {

    void ingredientCreated(IngredientCreatedEvent event);
}
