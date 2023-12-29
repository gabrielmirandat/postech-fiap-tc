package com.gabriel.products.adapter.driver.api.mappers;

import com.gabriel.products.adapter.driver.api.models.IngredientRequest;
import com.gabriel.products.adapter.driver.api.models.IngredientResponse;
import com.gabriel.products.adapter.driver.api.models.ProductCategoryDTO;
import com.gabriel.products.core.application.commands.CreateIngredientCommand;
import com.gabriel.products.core.domain.entities.Ingredient;
import com.gabriel.products.core.domain.entities.enums.Category;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IngredientMapper {

    public CreateIngredientCommand toCommand(IngredientRequest ingredientRequest) {
        return new CreateIngredientCommand(
            ingredientRequest.getName(),
            ingredientRequest.getPrice(),
            Category.valueOf(ingredientRequest.getCategory().name().toUpperCase()));
    }

    public IngredientResponse toResponse(Ingredient ingredient) {
        return new IngredientResponse()
            .id(ingredient.getIngredientID().getId())
            .name(ingredient.getName().getValue())
            .category(ProductCategoryDTO.valueOf(ingredient.getCategory().toString().toUpperCase()))
            .price(ingredient.getPrice().getValue());
    }
}
