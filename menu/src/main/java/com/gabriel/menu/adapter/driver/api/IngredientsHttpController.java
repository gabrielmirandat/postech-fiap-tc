package com.gabriel.menu.adapter.driver.api;

import com.gabriel.menu.adapter.driver.api.controller.IngredientsApi;
import com.gabriel.menu.adapter.driver.api.mapper.IngredientMapper;
import com.gabriel.menu.adapter.driver.api.models.IngredientCreated;
import com.gabriel.menu.adapter.driver.api.models.IngredientRequest;
import com.gabriel.menu.adapter.driver.api.models.IngredientResponse;
import com.gabriel.menu.adapter.driver.api.models.ProductCategoryDTO;
import com.gabriel.menu.core.application.command.CreateIngredientCommand;
import com.gabriel.menu.core.application.query.GetByIngredientIdQuery;
import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.application.usecase.CreateIngredientUseCase;
import com.gabriel.menu.core.application.usecase.RetrieveIngredientUseCase;
import com.gabriel.menu.core.application.usecase.SearchIngredientUseCase;
import com.gabriel.menu.core.domain.model.Ingredient;
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
