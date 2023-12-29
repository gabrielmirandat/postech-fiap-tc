package com.gabriel.products.adapter.driver.api.mappers;

import com.gabriel.products.adapter.driver.api.models.IngredientResponse;
import com.gabriel.products.adapter.driver.api.models.ProductCategoryDTO;
import com.gabriel.products.adapter.driver.api.models.ProductRequest;
import com.gabriel.products.adapter.driver.api.models.ProductResponse;
import com.gabriel.products.core.application.commands.CreateProductCommand;
import com.gabriel.products.core.domain.entities.Product;
import com.gabriel.products.core.domain.entities.enums.Category;
import com.gabriel.products.core.domain.valueobjects.ids.IngredientID;
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
            ingredients
        );
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse()
            .id(product.getProductID().getId())
            .name(product.getName().getValue())
            .description(product.getDescription().getValue())
            .ingredients(product.getIngredients().stream()
                .map(ingredient -> new IngredientResponse()
                    .id(ingredient.getId())
                    .name(ingredient.getId())
                    .category(ProductCategoryDTO.BURGER)
                    .price(10.0))
                .toList())
            .category(ProductCategoryDTO.valueOf(product.getCategory().toString().toUpperCase()))
            .price(product.getPrice().getValue());
    }
}


