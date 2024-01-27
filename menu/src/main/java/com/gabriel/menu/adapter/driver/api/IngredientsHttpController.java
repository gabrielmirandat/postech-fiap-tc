package com.gabriel.menu.adapter.driver.api;

import com.gabriel.menu.adapter.driver.api.mapper.IngredientMapper;
import com.gabriel.menu.core.application.command.CreateIngredientCommand;
import com.gabriel.menu.core.application.query.GetByIngredientIdQuery;
import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.application.usecase.IngredientUseCase;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.specs.menu.IngredientsApi;
import com.gabriel.specs.menu.models.IngredientCreated;
import com.gabriel.specs.menu.models.IngredientRequest;
import com.gabriel.specs.menu.models.IngredientResponse;
import com.gabriel.specs.menu.models.ProductCategoryDTO;
import jakarta.inject.Inject;

import java.util.List;

public class IngredientsHttpController implements IngredientsApi {

    @Inject
    IngredientUseCase ingredientUseCase;

    @Inject
    IngredientMapper ingredientMapper;


    @Override
    public IngredientCreated addIngredient(IngredientRequest ingredientRequest) {
        CreateIngredientCommand command = ingredientMapper.toCommand(ingredientRequest);
        Ingredient newIngredient = ingredientUseCase.createIngredient(command);
        return new IngredientCreated().ingredientId(newIngredient.getIngredientID().getId());
    }

    @Override
    public IngredientResponse getIngredientById(String ingredientId) {
        GetByIngredientIdQuery query = new GetByIngredientIdQuery(ingredientId);
        Ingredient ingredient = ingredientUseCase.getIngredientById(query);
        return ingredientMapper.toResponse(ingredient);
    }

    @Override
    public List<IngredientResponse> findIngredientsByQuery(ProductCategoryDTO category) {
        SearchIngredientQuery query = new SearchIngredientQuery(category.toString());
        List<Ingredient> ingredients = ingredientUseCase.searchIngredient(query);
        return ingredientMapper.toResponse(ingredients);
    }
}
