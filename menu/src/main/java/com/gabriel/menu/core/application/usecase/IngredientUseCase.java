package com.gabriel.menu.core.application.usecase;

import com.gabriel.menu.core.application.command.CreateIngredientCommand;
import com.gabriel.menu.core.application.query.GetByIngredientIdQuery;
import com.gabriel.menu.core.application.query.GetByIngredientIdsQuery;
import com.gabriel.menu.core.application.query.SearchIngredientQuery;
import com.gabriel.menu.core.domain.event.IngredientCreatedEvent;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.port.IngredientPublisher;
import com.gabriel.menu.core.domain.port.IngredientRepository;
import com.gabriel.menu.core.domain.port.SearchParameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class IngredientUseCase {

    @Inject
    IngredientRepository ingredientRepository;

    @Inject
    IngredientPublisher ingredientPublisher;


    public Ingredient createIngredient(CreateIngredientCommand command) {
        Ingredient ingredient = new Ingredient(command.name(), command.category(),
            command.price(), command.weight(), command.isExtra());
        ingredientRepository.saveIngredient(ingredient);
        ingredientPublisher.ingredientCreated(new IngredientCreatedEvent(ingredient));
        return ingredient;
    }

    public Ingredient getIngredientById(GetByIngredientIdQuery query) {
        return ingredientRepository.getById(query.searchId());
    }

    public List<Ingredient> getIngredientsById(GetByIngredientIdsQuery query) {
        return query.searchIds().stream()
            .map(id -> getIngredientById(new GetByIngredientIdQuery(id)))
            .collect(Collectors.toList());
    }

    public List<Ingredient> searchIngredient(SearchIngredientQuery query) {
        SearchParameters parameters = new SearchParameters(query.category());
        return ingredientRepository.searchBy(parameters);
    }
}
