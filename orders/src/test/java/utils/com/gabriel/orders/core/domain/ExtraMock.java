package utils.com.gabriel.orders.core.domain;

import com.gabriel.core.domain.model.Name;
import com.gabriel.core.domain.model.Price;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.orders.core.domain.model.Extra;

import java.time.Instant;

import static utils.com.gabriel.orders.core.UtilsMock.generateRandomString;

public class ExtraMock {

    public static Extra validExtra(IngredientID ingredientID) {
        return new Extra(ingredientID, new Name(generateRandomString()),
            new Price(2.0), Instant.now());
    }
}
