package com.gabriel.orders.core.domain;

import com.gabriel.model.IngredientId;
import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.orders.core.domain.model.Extra;

import java.time.Instant;

import static com.gabriel.orders.core.UtilsMock.generateRandomString;

public class ExtraMock {

    public static Extra validExtra(IngredientId ingredientID) {
        return Extra.create(ingredientID, Name.newBuilder().setValue(generateRandomString()).build(),
            Price.newBuilder().setValue(2.0).build(), Instant.now());
    }
}
