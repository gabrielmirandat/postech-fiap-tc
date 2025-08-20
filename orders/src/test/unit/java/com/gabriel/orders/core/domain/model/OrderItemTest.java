package com.gabriel.orders.core.domain.model;

import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.model.Model;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.OrderItem;
import com.gabriel.orders.core.domain.model.Product;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderItemTest {

    @Test
    void shouldCreateOrderItemSuccessfully_whenValidProductIsProvided() {
        // Arrange
        Product validProduct = Product.create(ProductId.newBuilder().setValue("12345678-PRDC-2024-12-20").build(), "Product", 10.0);

        // Act
        OrderItem orderItem = OrderItem.create(validProduct);

        // Assert
        assertThat(orderItem).isNotNull();
        assertThat(orderItem.getItemID()).isNotNull();
        assertThat(orderItem.getProduct()).isEqualTo(validProduct);
        assertThat(orderItem.getExtras()).isEmpty();
    }

    @Test
    void shouldCreateOrderItemSuccessfully_whenValidProductAndExtrasAreProvided() {
        // Arrange
        Product validProduct = Product.create(ProductId.newBuilder().setValue("12345678-PRDC-2024-12-20").build(), "Product", 10.0);
        Extra validExtra = Extra.create(IngredientId.newBuilder().setValue("87654321-INGR-2024-12-20").build(), "Extra", 2.0);

        // Act
        OrderItem orderItem = OrderItem.create(validProduct, Collections.singletonList(validExtra));

        // Assert
        assertThat(orderItem).isNotNull();
        assertThat(orderItem.getItemID()).isNotNull();
        assertThat(orderItem.getProduct()).isEqualTo(validProduct);
        assertThat(orderItem.getExtras()).isNotEmpty();
        assertThat(orderItem.getExtras()).contains(validExtra);
    }

    @Test
    void shouldThrowException_whenProductContainInvalidData() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> OrderItem.create(Product.create(ProductId.newBuilder().setValue("product-123").build(), "", 0.0)))
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Validation error");
    }

    @Test
    void shouldThrowException_whenExtrasContainInvalidData() {
        // Arrange
        Product validProduct = Product.create(ProductId.newBuilder().setValue("12345678-PRDC-2024-12-20").build(), "Product", 10.0);

        // Act & Assert
        assertThatThrownBy(() -> OrderItem.create(validProduct, List.of(Extra.create(IngredientId.newBuilder().setValue("87654321-INGR-2024-12-20").build(), "", 0.0))))
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Validation error");
    }
}
