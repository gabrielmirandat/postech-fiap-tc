package com.gabriel.menu.adapter.driver.api.mapper;

import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.menu.core.application.command.CreateIngredientCommand;
import com.gabriel.menu.core.application.command.CreateProductCommand;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.model.Product;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MenuMapper {

    public static CreateIngredientCommand toCommand(com.gabriel.specs.menu.models.IngredientRequest ingredientRequest) {
        return new CreateIngredientCommand(
            ingredientRequest.getName(),
            Category.valueOf(ingredientRequest.getCategory().name().toUpperCase()),
            ingredientRequest.getPrice(),
            ingredientRequest.getWeight(),
            ingredientRequest.getIsExtra());
    }

    public static com.gabriel.specs.menu.models.IngredientResponse toResponse(Ingredient ingredient) {
        return new com.gabriel.specs.menu.models.IngredientResponse()
            .id(ingredient.getIngredientID().getId())
            .name(ingredient.getName().getValue())
            .price(ingredient.getPrice().getValue())
            .category(com.gabriel.specs.menu.models.ProductCategoryDTO.valueOf(ingredient.getCategory().toString().toUpperCase()))
            .weight(ingredient.getWeight().getValue())
            .isExtra(ingredient.isExtra());
    }

    public static List<com.gabriel.specs.menu.models.IngredientResponse> toResponse(List<Ingredient> ingredients) {
        return ingredients.stream().map(MenuMapper::toResponse).collect(Collectors.toList());
    }

    public static CreateProductCommand toCommand(com.gabriel.specs.menu.models.ProductRequest request) {
        // convert from ProductIngredientRequest to List<IngredientID> considering
        // that quantity attribute defines the number of times ingredientId should be added to the list
        // e.g. if ingredientId = 1 and quantity = 3, then the list should contain 3 times the ingredientId
        // using IntStream.range and mapToObj
        List<IngredientID> ingredients = request.getIngredients().stream()
            .flatMap(in -> IntStream.range(0, in.getQuantity())
                .mapToObj(dump -> new IngredientID(in.getIngredientId())))
            .toList();

        return new CreateProductCommand(
            request.getName(),
            request.getPrice().doubleValue(),
            Category.valueOf(request.getCategory().toString().toUpperCase()),
            request.getDescription(),
            request.getImage(),
            ingredients
        );
    }

    public static com.gabriel.specs.menu.models.ProductResponse toResponse(Product product, List<Ingredient> ingredientList) {
        return new com.gabriel.specs.menu.models.ProductResponse()
            .id(product.getProductID().getId())
            .name(product.getName().getValue())
            .category(com.gabriel.specs.menu.models.ProductCategoryDTO.valueOf(product.getCategory().toString().toUpperCase()))
            .price(product.getPrice().getValue())
            .description(product.getDescription().getValue())
            .image(product.getImage().getUrl())
            .ingredients(ingredientList.stream()
                .map(ingredient -> new com.gabriel.specs.menu.models.IngredientResponse()
                    .id(ingredient.getIngredientID().getId())
                    .name(ingredient.getName().getValue())
                    .category(com.gabriel.specs.menu.models.ProductCategoryDTO.valueOf(ingredient.getCategory().toString().toUpperCase()))
                    .price(ingredient.getPrice().getValue())
                    .weight(ingredient.getWeight().getValue())
                    .isExtra(ingredient.isExtra()))
                .toList());

    }

    public static com.gabriel.specs.menu.models.ErrorResponse toErrorResponse(com.gabriel.adapter.api.exceptions.BaseHttpException exception) {
        return new com.gabriel.specs.menu.models.ErrorResponse()
            .status(exception.getStatus())
            .message(exception.getMessage())
            .code(exception.getCode());
    }
}
