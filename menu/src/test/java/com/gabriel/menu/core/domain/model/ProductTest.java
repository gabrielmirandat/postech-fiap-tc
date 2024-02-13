package com.gabriel.menu.core.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabriel.menu.core.MenuMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = MenuMock.generateProduct();
    }

    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }


    @Test
    void testProductCreation() {
        assertEquals("aProduct", product.getName().getValue());
        assertEquals(Category.BURGER, product.getCategory());
        assertEquals(1.0, product.getPrice().getValue());
        assertEquals("aDescription", product.getDescription().getValue());
        assertEquals("product.png", product.getImage().getUrl());
    }

    @Test
    void testProductCopy() {
        Product copy = Product.copy(product.getProductID(), product.getName(), product.getPrice(), product.getCategory(),
            product.getDescription(), product.getImage(), product.getIngredients(),
            product.getCreationTimestamp(), product.getUpdateTimestamp());
        assertEquals(product.getProductID(), copy.getProductID());
        assertEquals(product.getName(), copy.getName());
        assertEquals(product.getPrice(), copy.getPrice());
        assertEquals(product.getCategory(), copy.getCategory());
        assertEquals(product.getDescription(), copy.getDescription());
        assertEquals(product.getImage(), copy.getImage());
        assertEquals(product.getIngredients(), copy.getIngredients());
        assertEquals(product.getCreationTimestamp(), copy.getCreationTimestamp());
        assertEquals(product.getUpdateTimestamp(), copy.getUpdateTimestamp());
    }

    @Test
    void testSerializeProduct() throws Exception {
        byte[] serialized = product.serialized(objectMapper());
        assertNotNull(serialized);
    }

    @Test
    void testDeserializeProduct() {
        byte[] serialized = product.serialized(objectMapper());
        Product deserialized = Product.deserialize(objectMapper(), serialized);
        assertEquals(product.getProductID(), deserialized.getProductID());
        assertEquals(product.getName().getValue(), deserialized.getName().getValue());
        assertEquals(product.getPrice().getValue(), deserialized.getPrice().getValue());
        assertEquals(product.getCategory(), deserialized.getCategory());
        assertEquals(product.getDescription().getValue(), deserialized.getDescription().getValue());
        assertEquals(product.getImage().getUrl(), deserialized.getImage().getUrl());
        assertEquals(product.getIngredients(), deserialized.getIngredients());
        assertEquals(product.getCreationTimestamp(), deserialized.getCreationTimestamp());
        assertEquals(product.getUpdateTimestamp(), deserialized.getUpdateTimestamp());
    }
}
