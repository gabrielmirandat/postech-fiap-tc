package com.gabriel.products.core.application.usecases;

import com.gabriel.products.core.application.commands.CreateIngredientCommand;
import com.gabriel.products.core.domain.entities.Ingredient;
import com.gabriel.products.core.domain.events.IngredientCreatedEvent;
import com.gabriel.products.core.domain.ports.IngredientPublisher;
import com.gabriel.products.core.domain.ports.IngredientRepository;
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
