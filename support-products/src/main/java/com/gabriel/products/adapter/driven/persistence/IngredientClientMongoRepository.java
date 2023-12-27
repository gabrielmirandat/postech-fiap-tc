package com.gabriel.products.adapter.driven.persistence;

import com.gabriel.products.core.domain.entities.Ingredient;
import com.gabriel.products.core.domain.entities.Product;
import com.gabriel.products.core.domain.ports.IngredientRepository;
import com.gabriel.products.core.domain.ports.models.ProductSearchParameters;

import java.util.List;

public class IngredientClientMongoRepository implements IngredientRepository {


    @Override
    public Product saveIngredient(Ingredient ingredient) {
        return null;
    }

    @Override
    public Ingredient getById(String id) {
        return null;
    }

    @Override
    public List<Ingredient> searchBy(ProductSearchParameters parameters) {
        return null;
    }

    @Override
    public void deleteIngredient(String id) {

    }
}
