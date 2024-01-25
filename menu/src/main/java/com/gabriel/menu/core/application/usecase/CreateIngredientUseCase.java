package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.application.command.CreateIngredientCommand;
import com.gabriel.menu.core.domain.event.IngredientCreatedEvent;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.port.IngredientPublisher;
import com.gabriel.menu.core.domain.port.IngredientRepository;
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
        try {
            Ingredient ingredient = new Ingredient(command.name(), command.category(),
                command.price(), command.weight(), command.isExtra());
            ingredientRepository.saveIngredient(ingredient);
            ingredientPublisher.ingredientCreated(new IngredientCreatedEvent(ingredient));
            return ingredient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
