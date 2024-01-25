package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.application.query.GetByIngredientIdQuery;
import com.gabriel.menu.core.application.query.GetByIngredientIdsQuery;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.port.IngredientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RetrieveIngredientUseCase {

    @Inject
    IngredientRepository ingredientRepository;

    public Ingredient getIngredientById(GetByIngredientIdQuery query) {
        return ingredientRepository.getById(query.id());
    }

    public List<Ingredient> getIngredientsById(GetByIngredientIdsQuery query) {
        return query.ids().stream()
            .map(id -> getIngredientById(new GetByIngredientIdQuery(id)))
            .collect(Collectors.toList());
    }
}
