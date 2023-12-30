package com.gabriel.products.core.application.commands;

import com.gabriel.products.core.domain.entities.enums.Category;

public record CreateIngredientCommand(String name, Category category, Double price,
                                      Double weight, boolean isExtra) {
}
