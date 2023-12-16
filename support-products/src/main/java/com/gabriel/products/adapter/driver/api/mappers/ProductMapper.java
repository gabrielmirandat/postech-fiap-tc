package com.gabriel.products.adapter.driver.api.mappers;

import com.gabriel.products.adapter.driver.api.models.ProductRequest;
import com.gabriel.products.core.application.commands.CreateProductCommand;
import com.gabriel.products.core.domain.entities.enums.Category;
import com.gabriel.products.core.domain.valueobjects.ids.IngredientID;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.IntStream;

@ApplicationScoped
public class ProductMapper {

    // write a function to map from ProductRequest to CreateProductCommand
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
            Category.valueOf(request.getCategory().toString()),
            request.getDescription(),
            ingredients
        );
    }
}


