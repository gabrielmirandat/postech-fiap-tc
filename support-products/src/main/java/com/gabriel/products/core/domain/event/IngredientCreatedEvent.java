package com.gabriel.products.core.domain.event;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.common.core.domain.base.DomainEvent;
import com.gabriel.products.core.domain.model.Ingredient;
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
    public byte[] payload() throws JsonProcessingException {
        if (ingredientAdded == null) {
            throw new IllegalStateException("Ingredient is null");
        }
        return new ObjectMapper().writeValueAsBytes(ingredientAdded);
    }
}
