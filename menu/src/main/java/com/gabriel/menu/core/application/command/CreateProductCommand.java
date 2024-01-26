package com.gabriel.menu.core.application.command;

import com.gabriel.domain.model.id.IngredientID;
import com.gabriel.menu.core.domain.model.Category;

import java.util.List;

public record CreateProductCommand(String name, Double price, Category category,
                                   String description, String image, List<IngredientID> ingredients) {
}
