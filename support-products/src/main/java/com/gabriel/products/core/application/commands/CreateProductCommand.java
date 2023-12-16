package com.gabriel.products.core.application.commands;

import com.gabriel.products.core.domain.entities.enums.Category;
import com.gabriel.products.core.domain.valueobjects.ids.IngredientID;

import java.util.List;

public record CreateProductCommand(String name, Double price, Category category,
                                   String description, List<IngredientID> ingredients) {
}
