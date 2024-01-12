package com.gabriel.products.core.domain.port;

import com.gabriel.products.core.domain.model.Ingredient;

import java.util.List;

public interface IngredientRepository {

    Ingredient saveIngredient(Ingredient ingredient);

    Ingredient getById(String id);

    List<Ingredient> searchBy(ProductSearchParameters parameters);

    void deleteIngredient(String id);
}
