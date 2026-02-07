package com.gabriel.menu.core.domain.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.menu.core.domain.model.Ingredient;

public class IngredientCreatedEvent {

    Ingredient ingredientAdded;

    public IngredientCreatedEvent(Ingredient ingredientAdded) {
        this.ingredientAdded = ingredientAdded;
    }

    public String source() {
        return "post/ingredients";
    }

    public String subject() {
        return String.format("id/%s", ingredientAdded.getIngredientID().getId());
    }

    public String type() {
        return "postech.menu.v1.ingredient.created";
    }

    public byte[] payload(ObjectMapper serializer) {
        return ingredientAdded.serialized(serializer);
    }

    public String audience() {
        return "public";
    }

    public String context() {
        return "menu";
    }

    public Ingredient getIngredientAdded() {
        return ingredientAdded;
    }
}
