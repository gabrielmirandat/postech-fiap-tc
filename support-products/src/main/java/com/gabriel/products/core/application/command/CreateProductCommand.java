package com.gabriel.products.core.application.command;

import com.gabriel.common.core.domain.model.id.IngredientID;
import com.gabriel.products.core.domain.model.Category;

import java.util.List;

public record CreateProductCommand(String name, Double price, Category category,
                                   String description, String image, List<IngredientID> ingredients) {
}
