package com.gabriel.products.core.application.usecases;

import com.gabriel.products.core.application.queries.GetByIngredientIdQuery;
import com.gabriel.products.core.domain.entities.Ingredient;
import com.gabriel.products.core.domain.ports.IngredientRepository;
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
