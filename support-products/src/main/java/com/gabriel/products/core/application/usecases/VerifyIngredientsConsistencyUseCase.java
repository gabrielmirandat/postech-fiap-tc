package com.gabriel.products.core.application.usecases;

import com.gabriel.products.core.application.exceptions.IngredientDoNotExistException;
import com.gabriel.products.core.domain.entities.Ingredient;
import com.gabriel.products.core.domain.valueobjects.ids.IngredientID;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class VerifyIngredientsConsistencyUseCase {

    @Inject
    RetrieveIngredientMenuUseCase retrieveIngredientMenuUseCase;

    public void verifyIngredientsConsistency(List<IngredientID> inputIngredients) {
        List<IngredientID> ingredientMenuIds =
            retrieveIngredientMenuUseCase.retrieveIngredientMenu()
                .stream().map(Ingredient::getIngredientID).toList();

        for (IngredientID inputIngredient : inputIngredients) {
            if (!ingredientMenuIds.contains(inputIngredient)) {
                throw new IngredientDoNotExistException(inputIngredient.getId());
            }
        }
    }
}
