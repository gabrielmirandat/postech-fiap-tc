package com.gabriel.menu.integration;

import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.menu.core.domain.exception.MenuDomainException;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Domain Integration Tests")
class ProductDomainIntegrationTest {

    @Test
    @DisplayName("Should create product with valid data")
    void shouldCreateProductWithValidData() {
        Product product = new Product(
            "Deluxe Burger",
            25.99,
            Category.BURGER,
            "A delicious burger with premium ingredients",
            "https://example.com/burger.png",
            List.of(),
            List.of()
        );

        assertNotNull(product);
        assertEquals("Deluxe Burger", product.getNameString());
        assertEquals(25.99, product.getPriceValue());
        assertEquals(Category.BURGER, product.getCategory());
        assertEquals("A delicious burger with premium ingredients", product.getDescriptionString());
    }

    @Test
    @DisplayName("Should create product with ingredients")
    void shouldCreateProductWithIngredients() {
        IngredientId cheese = IngredientId.newBuilder().setValue("cheese-123").build();
        IngredientId lettuce = IngredientId.newBuilder().setValue("lettuce-456").build();

        Product product = new Product(
            "Cheese Burger",
            20.99,
            Category.BURGER,
            "Burger with cheese and lettuce",
            "https://example.com/cheeseburger.png",
            List.of(cheese, lettuce),
            List.of(cheese, lettuce)
        );

        assertNotNull(product);
        assertEquals(2, product.getIngredients().size());
        assertTrue(product.getIngredients().contains(cheese));
        assertTrue(product.getIngredients().contains(lettuce));
    }

    @Test
    @DisplayName("Should validate ingredients exist in all ingredients list")
    void shouldValidateIngredientsExistInAllIngredientsList() {
        IngredientId validIngredient = IngredientId.newBuilder().setValue("valid-123").build();
        IngredientId invalidIngredient = IngredientId.newBuilder().setValue("invalid-999").build();

        assertThrows(MenuDomainException.class, () -> {
            new Product(
                "Invalid Product",
                15.99,
                Category.BURGER,
                "Product with invalid ingredient",
                "https://example.com/invalid.png",
                List.of(invalidIngredient),
                List.of(validIngredient)
            );
        });
    }

    @Test
    @DisplayName("Should create products with different categories")
    void shouldCreateProductsWithDifferentCategories() {
        Product burger = new Product(
            "Burger",
            20.00,
            Category.BURGER,
            "Burger description",
            "image.png",
            List.of(),
            List.of()
        );

        Product dessert = new Product(
            "Ice Cream",
            10.00,
            Category.DESSERT,
            "Dessert description",
            "dessert.png",
            List.of(),
            List.of()
        );

        Product drink = new Product(
            "Soda",
            5.00,
            Category.DRINK,
            "Drink description",
            "drink.png",
            List.of(),
            List.of()
        );

        Product accompaniment = new Product(
            "Fries",
            8.00,
            Category.ACCOMPANIMENT,
            "Side dish",
            "fries.png",
            List.of(),
            List.of()
        );

        assertEquals(Category.BURGER, burger.getCategory());
        assertEquals(Category.DESSERT, dessert.getCategory());
        assertEquals(Category.DRINK, drink.getCategory());
        assertEquals(Category.ACCOMPANIMENT, accompaniment.getCategory());
    }

    @Test
    @DisplayName("Should copy product correctly")
    void shouldCopyProductCorrectly() {
        Product original = new Product(
            "Original Product",
            30.00,
            Category.BURGER,
            "Original description",
            "original.png",
            List.of(),
            List.of()
        );

        Product copy = Product.copy(
            original.getProductId(),
            original.getName(),
            original.getPrice(),
            original.getCategory(),
            original.getDescription(),
            original.getImage(),
            original.getIngredients(),
            original.getCreationTimestamp(),
            original.getUpdateTimestamp()
        );

        assertNotNull(copy);
        assertEquals(original.getProductId(), copy.getProductId());
        assertEquals(original.getNameString(), copy.getNameString());
        assertEquals(original.getPriceValue(), copy.getPriceValue());
        assertEquals(original.getCategory(), copy.getCategory());
    }

    // @Test
    // @DisplayName("Should have auto-generated product ID")
    // void shouldHaveAutoGeneratedProductId() {
    //     Product product1 = new Product(
    //         "Product 1",
    //         10.00,
    //         Category.BURGER,
    //         "Description 1",
    //         "image1.png",
    //         List.of(),
    //         List.of()
    //     );
    //
    //     Product product2 = new Product(
    //         "Product 2",
    //         20.00,
    //         Category.DESSERT,
    //         "Description 2",
    //         "image2.png",
    //         List.of(),
    //         List.of()
    //     );
    //
    //     assertNotNull(product1.getProductId());
    //     assertNotNull(product2.getProductId());
    //     assertNotEquals(product1.getProductId(), product2.getProductId());
    // }

    // @Test
    // @DisplayName("Should have timestamps")
    // void shouldHaveTimestamps() {
    //     Product product = new Product(
    //         "Product",
    //         10.00,
    //         Category.BURGER,
    //         "Description",
    //         "image.png",
    //         List.of(),
    //         List.of()
    //     );
    //
    //     assertNotNull(product.getCreationTimestamp());
    //     assertNotNull(product.getUpdateTimestamp());
    // }
}
