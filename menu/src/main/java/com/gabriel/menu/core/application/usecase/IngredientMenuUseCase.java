package com.gabriel.menu.core.application.usecase;

import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.menu.core.application.exception.IngredientInvalidException;
import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@ApplicationScoped
public class IngredientMenuUseCase {

    @Inject
    IngredientUseCase ingredientUseCase;

    private List<IngredientID> idsOf(List<Ingredient> ingredients) {
        return ingredients.stream().map(Ingredient::getIngredientID).toList();
    }

    private void match(List<IngredientID> inputIngredients, List<IngredientID> allIngredients) {
        if (!new HashSet<>(allIngredients).containsAll(inputIngredients)) {
            throw new IngredientInvalidException("Some of the ingredients are invalid.");
        }
    }

    public List<Ingredient> retrieveMenu() {
        List<Ingredient> ingredients = new ArrayList<>();

        for (Category category : Category.values()) {
            ingredients.addAll(ingredientUseCase.searchIngredient(new SearchIngredientQuery(category.name())));
        }
        return ingredients;
    }

    public void matchToMenu(List<IngredientID> inputIngredients) {
        match(inputIngredients, idsOf(retrieveMenu()));
    }

    public void matchToMenuByCategory(Category category, List<IngredientID> inputIngredients) {
        match(inputIngredients, idsOf(
            ingredientUseCase.searchIngredient(
                new SearchIngredientQuery(category.name()))));
    }
}
