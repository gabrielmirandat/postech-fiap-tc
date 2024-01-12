package com.gabriel.products.core.application.usecase;

import com.gabriel.products.core.application.query.GetByIngredientIdQuery;
import com.gabriel.products.core.domain.model.Ingredient;
import com.gabriel.products.core.domain.port.IngredientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RetrieveIngredientUseCase {

    @Inject
    IngredientRepository ingredientRepository;

    public Ingredient getIngredientById(GetByIngredientIdQuery query) {
        return ingredientRepository.getById(query.id());
    }
}
