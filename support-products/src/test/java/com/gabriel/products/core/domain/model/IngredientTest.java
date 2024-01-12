package com.gabriel.products.core.domain.model;

import org.junit.jupiter.api.Test;

class IngredientTest {

    @Test
    void shouldCreateIngredient() {
        // given
        var name = "name";
        var category = Category.BURGER;
        var price = 1.0;
        var weight = 1.0;
        var isExtra = true;

        // when
        var ingredient = new Ingredient(name, category, price, weight, isExtra);

        // then
        assert ingredient != null;
    }
}
