package com.gabriel.products.core.domain.events;

import com.gabriel.products.core.domain.base.DomainEvent;
import com.gabriel.products.core.domain.entities.Ingredient;
import lombok.Getter;

@Getter
public class IngredientCreatedEvent implements DomainEvent {

    Ingredient ingredientAdded;

    public IngredientCreatedEvent(Ingredient ingredientAdded) {
        this.ingredientAdded = ingredientAdded;
    }

    @Override
    public String source() {
        return "post/ingredients";
    }

    @Override
    public String subject() {
        return String.format("id/%s", ingredientAdded.getIngredientID().getId());
    }

    @Override
    public String type() {
        return "postech.menu.v1.ingredient.created";
    }
}
