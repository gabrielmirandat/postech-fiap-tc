package com.gabriel.products.core.application.usecase;

import com.gabriel.common.core.domain.model.id.IngredientID;
import com.gabriel.products.core.application.exception.IngredientDoNotExistException;
import com.gabriel.products.core.domain.model.Ingredient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class VerifyIngredientUseCase {

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
