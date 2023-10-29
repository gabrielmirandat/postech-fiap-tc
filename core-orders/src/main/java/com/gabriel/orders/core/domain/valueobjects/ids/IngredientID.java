package com.gabriel.orders.core.domain.valueobjects.ids;

import com.gabriel.orders.core.domain.entities.enums.EntityType;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class IngredientID extends EntityID {

    @Pattern(regexp = "[0-9a-f]{8}-INGR-\\d{4}-\\d{2}-\\d{2}",
            message = "Invalid Product ID format")
    private final String id;

    public IngredientID() {
        this.id = generate(EntityType.INGREDIENT);
        validateSelf();
    }

    public IngredientID(String id) {
        this.id = id;
        validateSelf();
    }
}
