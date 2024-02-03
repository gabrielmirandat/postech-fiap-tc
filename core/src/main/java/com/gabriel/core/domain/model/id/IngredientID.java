package com.gabriel.core.domain.model.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.gabriel.core.domain.EntityType;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class IngredientID extends EntityID {

    @JsonValue
    @Pattern(regexp = "[0-9a-f]{8}-INGR-\\d{4}-\\d{2}-\\d{2}",
        message = "Invalid Product ID format")
    private final String id;

    public IngredientID() {
        this.id = generate(EntityType.INGREDIENT);
        validate();
    }

    public IngredientID(String id) {
        this.id = id;
        validate();
    }
}
