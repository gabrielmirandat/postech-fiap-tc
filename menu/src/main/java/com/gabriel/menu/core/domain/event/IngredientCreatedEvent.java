package com.gabriel.menu.core.domain.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.DomainEvent;
import com.gabriel.menu.core.domain.model.Ingredient;
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

    @Override
    public byte[] payload(ObjectMapper serializer) {
        return ingredientAdded.serialized(serializer);
    }
}
