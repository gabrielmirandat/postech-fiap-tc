package com.gabriel.menu.core.application.query;

import com.gabriel.core.domain.model.id.IngredientID;

import java.util.List;

public record GetByIngredientIdsQuery(List<IngredientID> searchIds) {
}
