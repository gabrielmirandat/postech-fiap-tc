package com.gabriel.products.core.application.usecases;

import com.gabriel.products.core.application.queries.SearchIngredientQuery;
import com.gabriel.products.core.domain.entities.Ingredient;
import com.gabriel.products.core.domain.entities.enums.Category;
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
