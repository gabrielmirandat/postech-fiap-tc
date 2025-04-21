package com.gabriel.menu.core.application.query;

import com.gabriel.model.IngredientId;

import java.util.List;

public record GetByIngredientIdsQuery(List<IngredientId> searchIds) {
}
