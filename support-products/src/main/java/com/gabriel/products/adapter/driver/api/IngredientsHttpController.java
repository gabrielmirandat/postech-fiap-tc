package com.gabriel.products.adapter.driver.api;

import com.gabriel.products.adapter.driver.api.controller.IngredientsApi;
import com.gabriel.products.adapter.driver.api.mappers.IngredientMapper;
import com.gabriel.products.adapter.driver.api.models.IngredientCreated;
import com.gabriel.products.adapter.driver.api.models.IngredientRequest;
import com.gabriel.products.adapter.driver.api.models.IngredientResponse;
import com.gabriel.products.adapter.driver.api.models.ProductCategoryDTO;
import com.gabriel.products.core.application.commands.CreateIngredientCommand;
import com.gabriel.products.core.application.queries.GetByIngredientIdQuery;
import com.gabriel.products.core.application.queries.SearchIngredientQuery;
import com.gabriel.products.core.application.usecases.CreateIngredientUseCase;
import com.gabriel.products.core.application.usecases.RetrieveIngredientUseCase;
import com.gabriel.products.core.application.usecases.SearchIngredientUseCase;
import com.gabriel.products.core.domain.entities.Ingredient;
import jakarta.inject.Inject;

import java.util.List;

public class IngredientsHttpController implements IngredientsApi {

    @Inject
    CreateIngredientUseCase createIngredientUseCase;

    @Inject
    RetrieveIngredientUseCase retrieveIngredientUseCase;

    @Inject
    SearchIngredientUseCase searchIngredientUseCase;

    @Inject
    IngredientMapper ingredientMapper;


    @Override
    public IngredientCreated addIngredient(IngredientRequest ingredientRequest) {
        CreateIngredientCommand command = ingredientMapper.toCommand(ingredientRequest);
        Ingredient newIngredient = createIngredientUseCase.createIngredient(command);
        return new IngredientCreated().ingredientId(newIngredient.getIngredientID().getId());
    }

    @Override
    public IngredientResponse getIngredientById(String ingredientId) {
        GetByIngredientIdQuery query = new GetByIngredientIdQuery(ingredientId);
        Ingredient ingredient = retrieveIngredientUseCase.getIngredientById(query);
        return ingredientMapper.toResponse(ingredient);
    }

    @Override
    public List<IngredientResponse> findIngredientsByQuery(ProductCategoryDTO category) {
        SearchIngredientQuery query = new SearchIngredientQuery(category.toString());
        List<Ingredient> ingredients = searchIngredientUseCase.searchIngredient(query);
        return ingredientMapper.toResponse(ingredients);
    }
}
