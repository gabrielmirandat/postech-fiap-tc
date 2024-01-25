package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RetrieveIngredientMenuUseCase {

    @Inject
    SearchIngredientUseCase searchIngredientUseCase;

    public List<Ingredient> retrieveIngredientMenu() {
        List<Ingredient> ingredients = new ArrayList<>();

        for (Category category : Category.values()) {
            ingredients.addAll(searchIngredientUseCase.searchIngredient(new SearchIngredientQuery(category.name())));
        }
        return ingredients;
    }
}
