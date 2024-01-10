package com.gabriel.orders.core.application.events;

import com.gabriel.orders.core.domain.valueobjects.ids.IngredientID;

public record MenuIngredientAddedEvent(IngredientID ingredientAdded) {
}
