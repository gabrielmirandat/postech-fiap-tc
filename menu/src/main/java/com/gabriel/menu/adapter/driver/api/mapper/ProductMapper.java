package com.gabriel.menu.adapter.driver.api.mapper;

import com.gabriel.domain.model.id.IngredientID;
import com.gabriel.menu.core.application.command.CreateProductCommand;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.model.Product;
import com.gabriel.specs.menu.models.IngredientResponse;
import com.gabriel.specs.menu.models.ProductCategoryDTO;
import com.gabriel.specs.menu.models.ProductRequest;
import com.gabriel.specs.menu.models.ProductResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.IntStream;

@ApplicationScoped
public class ProductMapper {

    public CreateProductCommand toCommand(ProductRequest request) {
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

    public ProductResponse toResponse(Product product, List<Ingredient> ingredientList) {
        return new ProductResponse()
            .id(product.getProductID().getId())
            .name(product.getName().getValue())
            .category(ProductCategoryDTO.valueOf(product.getCategory().toString().toUpperCase()))
            .price(product.getPrice().getValue())
            .description(product.getDescription().getValue())
            .image(product.getImage().getUrl())
            .ingredients(ingredientList.stream()
                .map(ingredient -> new IngredientResponse()
                    .id(ingredient.getIngredientID().getId())
                    .name(ingredient.getName().getValue())
                    .category(ProductCategoryDTO.valueOf(ingredient.getCategory().toString().toUpperCase()))
                    .price(ingredient.getPrice().getValue())
                    .weight(ingredient.getWeight().getValue())
                    .isExtra(ingredient.isExtra()))
                .toList());

    }
}


