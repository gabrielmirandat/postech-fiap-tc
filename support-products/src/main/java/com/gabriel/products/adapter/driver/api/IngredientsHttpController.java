package com.gabriel.products.adapter.driver.api;

import com.gabriel.products.adapter.driver.api.controller.IngredientsApi;
import com.gabriel.products.adapter.driver.api.models.IngredientCreated;
import com.gabriel.products.adapter.driver.api.models.IngredientRequest;
import com.gabriel.products.adapter.driver.api.models.IngredientResponse;
import com.gabriel.products.adapter.driver.api.models.ProductCategoryDTO;
import com.gabriel.products.core.application.commands.CreateIngredientCommand;
import com.gabriel.products.core.application.usecases.CreateIngredientUseCase;
import com.gabriel.products.core.domain.entities.Ingredient;
import com.gabriel.products.core.domain.entities.enums.Category;
import jakarta.inject.Inject;

public class IngredientsHttpController implements IngredientsApi {

    @Inject
    CreateIngredientUseCase createIngredientUseCase;


    @Override
    public IngredientCreated addIngredient(IngredientRequest ingredientRequest) {
        CreateIngredientCommand command =
            new CreateIngredientCommand(
                ingredientRequest.getName(),
                ingredientRequest.getPrice(),
                Category.valueOf(ingredientRequest.getCategory().name().toUpperCase()));
        Ingredient newIngredient = createIngredientUseCase.createIngredient(command);
        return new IngredientCreated().ingredientId(newIngredient.getIngredientID().getId());
    }

    @Override
    public IngredientResponse findIngredientsByQuery(ProductCategoryDTO category) {
        return null;
    }
}
