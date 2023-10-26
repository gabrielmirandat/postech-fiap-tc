package com.gabriel.products.adapter.driver.api.controllers;

import com.gabriel.products.adapter.driver.api.controllers.models.IngredientCreated;
import com.gabriel.products.adapter.driver.api.controllers.models.IngredientRequest;
import com.gabriel.products.adapter.driver.api.controllers.models.IngredientResponse;
import com.gabriel.products.adapter.driver.api.controllers.models.ProductCategoryDTO;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class IngredientsController implements IngredientsApi {


    @Override
    public ResponseEntity<IngredientCreated> addIngredient(IngredientRequest ingredientRequest) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<IngredientResponse> findIngredientsByQuery(Optional<ProductCategoryDTO> category) throws Exception {
        return null;
    }
}
