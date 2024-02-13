package com.gabriel.menu.core.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabriel.menu.core.MenuMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IngredientTest {

    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        ingredient = MenuMock.generateIngredient(true);
    }

    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Test
    void testIngredientCreation() {
        assertEquals("anIngredient", ingredient.getName().getValue());
        assertEquals(Category.BURGER, ingredient.getCategory());
        assertEquals(1.0, ingredient.getPrice().getValue());
        assertEquals(1.0, ingredient.getWeight().getValue());
        Assertions.assertTrue(ingredient.isExtra());
    }

    @Test
    void testIngredientCopy() {
        Ingredient copy = Ingredient.copy(ingredient.getIngredientID(), ingredient.getName(), ingredient.getCategory(),
            ingredient.getPrice(), ingredient.getWeight(), ingredient.isExtra(), ingredient.getCreationTimestamp(),
            ingredient.getUpdateTimestamp());
        assertEquals(ingredient.getIngredientID(), copy.getIngredientID());
        assertEquals(ingredient.getName(), copy.getName());
        assertEquals(ingredient.getCategory(), copy.getCategory());
        assertEquals(ingredient.getPrice(), copy.getPrice());
        assertEquals(ingredient.getWeight(), copy.getWeight());
        assertEquals(ingredient.isExtra(), copy.isExtra());
        assertEquals(ingredient.getCreationTimestamp(), copy.getCreationTimestamp());
        assertEquals(ingredient.getUpdateTimestamp(), copy.getUpdateTimestamp());
    }

    @Test
    void testSerializeIngredient() throws Exception {
        byte[] serialized = ingredient.serialized(objectMapper());
        assertNotNull(serialized);
    }

    @Test
    void testDeserializeIngredient() {
        byte[] serialized = ingredient.serialized(objectMapper());
        Ingredient deserialized = Ingredient.deserialize(objectMapper(), serialized);
        assertNotNull(deserialized);
        assertEquals(ingredient.getIngredientID(), deserialized.getIngredientID());
        assertEquals(ingredient.getName().getValue(), deserialized.getName().getValue());
        assertEquals(ingredient.getCategory(), deserialized.getCategory());
        assertEquals(ingredient.getPrice().getValue(), deserialized.getPrice().getValue());
        assertEquals(ingredient.getWeight().getValue(), deserialized.getWeight().getValue());
        assertEquals(ingredient.isExtra(), deserialized.isExtra());
        assertEquals(ingredient.getCreationTimestamp(), deserialized.getCreationTimestamp());
        assertEquals(ingredient.getUpdateTimestamp(), deserialized.getUpdateTimestamp());
    }
}
