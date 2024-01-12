package com.gabriel.products.core.application.usecase;

import com.gabriel.products.core.application.command.CreateIngredientCommand;
import com.gabriel.products.core.domain.event.IngredientCreatedEvent;
import com.gabriel.products.core.domain.model.Ingredient;
import com.gabriel.products.core.domain.port.IngredientPublisher;
import com.gabriel.products.core.domain.port.IngredientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateIngredientUseCase {

    @Inject
    IngredientRepository ingredientRepository;

    @Inject
    IngredientPublisher ingredientPublisher;

    // it may not work with quarkus
    public Ingredient createIngredient(CreateIngredientCommand command) {
        Ingredient ingredient = new Ingredient(command.name(), command.category(),
            command.price(), command.weight(), command.isExtra());
        ingredientRepository.saveIngredient(ingredient);
        ingredientPublisher.ingredientCreated(new IngredientCreatedEvent(ingredient));
        return ingredient;
    }
}
