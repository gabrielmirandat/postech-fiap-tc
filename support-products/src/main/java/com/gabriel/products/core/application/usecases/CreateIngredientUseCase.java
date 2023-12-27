package com.gabriel.products.core.application.usecases;

import com.gabriel.products.core.application.commands.CreateIngredientCommand;
import com.gabriel.products.core.application.commands.CreateProductCommand;
import com.gabriel.products.core.domain.entities.Ingredient;
import com.gabriel.products.core.domain.entities.Product;
import com.gabriel.products.core.domain.ports.IngredientRepository;
import com.gabriel.products.core.domain.ports.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateIngredientUseCase {

    @Inject
    IngredientRepository ingredientRepository;

    // it may not work with quarkus
    public Ingredient createIngredient(CreateIngredientCommand command) {
        Ingredient ingredient = new Ingredient(command.name(), command.price(), command.category());
        ingredientRepository.saveIngredient(ingredient);
        return ingredient;
    }
}
