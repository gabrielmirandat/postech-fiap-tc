package com.gabriel.products.core.application.command;

import com.gabriel.products.core.domain.model.Category;

public record CreateIngredientCommand(String name, Category category, Double price,
                                      Double weight, boolean isExtra) {
}
