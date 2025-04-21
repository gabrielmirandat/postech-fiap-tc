package com.gabriel.menu.core.domain.port;

import com.gabriel.model.IngredientId;
import com.gabriel.menu.core.domain.model.Ingredient;

import java.util.List;

public interface IngredientRepository {

    Ingredient saveIngredient(Ingredient ingredient);

    Ingredient getById(IngredientId id);

    List<Ingredient> searchBy(SearchParameters parameters);

    void deleteIngredient(IngredientId id);
}
