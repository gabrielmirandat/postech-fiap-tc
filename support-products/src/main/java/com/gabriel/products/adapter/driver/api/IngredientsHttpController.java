package com.gabriel.products.adapter.driver.api;

import com.gabriel.products.adapter.driver.api.controller.IngredientsApi;
import com.gabriel.products.adapter.driver.api.models.IngredientCreated;
import com.gabriel.products.adapter.driver.api.models.IngredientRequest;
import com.gabriel.products.adapter.driver.api.models.IngredientResponse;
import com.gabriel.products.adapter.driver.api.models.ProductCategoryDTO;

public class IngredientsHttpController implements IngredientsApi {


    @Override
    public IngredientCreated addIngredient(IngredientRequest ingredientRequest) {
        return null;
    }

    @Override
    public IngredientResponse findIngredientsByQuery(ProductCategoryDTO category) {
        return null;
    }
}
