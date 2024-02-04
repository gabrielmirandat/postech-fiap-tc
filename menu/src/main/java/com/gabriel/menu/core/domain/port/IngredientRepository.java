package com.gabriel.menu.core.domain.port;

import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.menu.core.domain.model.Ingredient;

import java.util.List;

public interface IngredientRepository {

    Ingredient saveIngredient(Ingredient ingredient);

    Ingredient getById(IngredientID id);

    List<Ingredient> searchBy(SearchParameters parameters);

    void deleteIngredient(IngredientID id);
}
