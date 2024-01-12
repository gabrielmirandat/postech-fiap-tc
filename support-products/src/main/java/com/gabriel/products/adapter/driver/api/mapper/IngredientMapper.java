package com.gabriel.products.adapter.driver.api.mapper;

import com.gabriel.products.adapter.driver.api.models.IngredientRequest;
import com.gabriel.products.adapter.driver.api.models.IngredientResponse;
import com.gabriel.products.adapter.driver.api.models.ProductCategoryDTO;
import com.gabriel.products.core.application.command.CreateIngredientCommand;
import com.gabriel.products.core.domain.model.Category;
import com.gabriel.products.core.domain.model.Ingredient;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class IngredientMapper {

    public CreateIngredientCommand toCommand(IngredientRequest ingredientRequest) {
        return new CreateIngredientCommand(
            ingredientRequest.getName(),
            Category.valueOf(ingredientRequest.getCategory().name().toUpperCase()),
            ingredientRequest.getPrice(),
            ingredientRequest.getWeight(),
            ingredientRequest.getIsExtra());
    }

    public IngredientResponse toResponse(Ingredient ingredient) {
        return new IngredientResponse()
            .id(ingredient.getIngredientID().getId())
            .name(ingredient.getName().getValue())
            .price(ingredient.getPrice().getValue())
            .category(ProductCategoryDTO.valueOf(ingredient.getCategory().toString().toUpperCase()))
            .weight(ingredient.getWeight().getValue())
            .isExtra(ingredient.isExtra());
    }

    public List<IngredientResponse> toResponse(List<Ingredient> ingredients) {
        return ingredients.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
