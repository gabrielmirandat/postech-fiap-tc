package com.gabriel.menu.core.application.command;

import com.gabriel.menu.core.domain.model.Category;

public record CreateIngredientCommand(String name, Category category, Double price,
                                      Double weight, boolean isExtra) {
}
