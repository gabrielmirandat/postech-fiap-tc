package com.gabriel.menu.adapter.driver.api;

import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.menu.adapter.driver.api.mapper.MenuMapper;
import com.gabriel.menu.core.application.command.CreateIngredientCommand;
import com.gabriel.menu.core.application.query.GetByIngredientIdQuery;
import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.application.usecase.IngredientUseCase;
import com.gabriel.menu.core.domain.model.Category;
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

    @Override
    public IngredientCreated addIngredient(IngredientRequest ingredientRequest) {
        CreateIngredientCommand command = MenuMapper.toCommand(ingredientRequest);
        Ingredient newIngredient = ingredientUseCase.createIngredient(command);
        return new IngredientCreated().ingredientId(newIngredient.getIngredientID().getId());
    }

    @Override
    public IngredientResponse getIngredientById(String ingredientId) {
        GetByIngredientIdQuery query = new GetByIngredientIdQuery(new IngredientID(ingredientId));
        Ingredient ingredient = ingredientUseCase.getIngredientById(query);
        return MenuMapper.toResponse(ingredient);
    }

    // TODO: invalid ENUM dto comming as 404
    @Override
    public List<IngredientResponse> findIngredientsByQuery(ProductCategoryDTO category) {
        SearchIngredientQuery query = new SearchIngredientQuery(Category.valueOf(category.name().toUpperCase()));
        List<Ingredient> ingredients = ingredientUseCase.searchIngredient(query);
        return MenuMapper.toResponse(ingredients);
    }
}
