package com.gabriel.menu.core.application.usecase;

import com.gabriel.domain.model.id.IngredientID;
import com.gabriel.menu.core.application.exception.IngredientInvalidException;
import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.List;

@ApplicationScoped
public class VerifyIngredientMenuUseCase {

    @Inject
    RetrieveIngredientMenuUseCase retrieveIngredientMenuUseCase;

    @Inject
    SearchIngredientUseCase searchIngredientUseCase;

    private List<IngredientID> idsOf(List<Ingredient> ingredients) {
        return ingredients.stream().map(Ingredient::getIngredientID).toList();
    }

    private void match(List<IngredientID> inputIngredients, List<IngredientID> ingredientMenuIds) {
        if (!new HashSet<>(ingredientMenuIds).containsAll(inputIngredients)) {
            throw new IngredientInvalidException("Some of the ingredients are invalid.");
        }
    }

    public void validate(List<IngredientID> inputIngredients) {
        match(inputIngredients,
            idsOf(retrieveIngredientMenuUseCase.retrieveIngredientMenu()));
    }

    public void validate(Category category, List<IngredientID> inputIngredients) {
        match(inputIngredients,
            idsOf(searchIngredientUseCase.searchIngredient(
                new SearchIngredientQuery(category.name()))));
    }

}
