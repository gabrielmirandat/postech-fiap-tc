package com.gabriel.products.core.domain.ports;

import com.gabriel.products.core.domain.entities.Ingredient;
import com.gabriel.products.core.domain.ports.models.ProductSearchParameters;

import java.util.List;

public interface IngredientRepository {

    Ingredient saveIngredient(Ingredient ingredient);

    Ingredient getById(String id);

    List<Ingredient> searchBy(ProductSearchParameters parameters);

    void deleteIngredient(String id);
}
