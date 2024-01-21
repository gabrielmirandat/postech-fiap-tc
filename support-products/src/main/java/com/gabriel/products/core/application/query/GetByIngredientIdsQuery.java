package com.gabriel.products.core.application.query;

import java.util.List;

public record GetByIngredientIdsQuery(List<String> ids) {
}
