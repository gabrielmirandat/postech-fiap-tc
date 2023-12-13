package com.gabriel.products.adapter.driver.api;

import com.gabriel.products.adapter.driver.api.controllers.IngredientsApi;
import com.gabriel.products.adapter.driver.api.controllers.models.IngredientCreated;
import com.gabriel.products.adapter.driver.api.controllers.models.IngredientRequest;
import com.gabriel.products.adapter.driver.api.controllers.models.IngredientResponse;
import com.gabriel.products.adapter.driver.api.controllers.models.ProductCategoryDTO;

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
