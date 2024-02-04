package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class IngredientMenuUseCase {

    @Inject
    IngredientUseCase ingredientUseCase;

    public List<Ingredient> retrieveMenu() {
        List<Ingredient> ingredients = new ArrayList<>();

        for (Category category : Category.values()) {
            ingredients.addAll(ingredientUseCase.searchIngredient(new SearchIngredientQuery(category)));
        }
        return ingredients;
    }
}
