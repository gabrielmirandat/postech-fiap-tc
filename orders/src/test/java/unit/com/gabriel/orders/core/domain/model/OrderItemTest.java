package unit.com.gabriel.orders.core.domain.model;

import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
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
        Product validProduct = new Product(new ProductID(), "Product", 10.0);

        // Act
        OrderItem orderItem = new OrderItem(validProduct);

        // Assert
        assertThat(orderItem).isNotNull();
        assertThat(orderItem.getItemID()).isNotNull();
        assertThat(orderItem.getProduct()).isEqualTo(validProduct);
        assertThat(orderItem.getExtras()).isEmpty();
    }

    @Test
    void shouldCreateOrderItemSuccessfully_whenValidProductAndExtrasAreProvided() {
        // Arrange
        Product validProduct = new Product(new ProductID(), "Product", 10.0);
        Extra validExtra = new Extra(new IngredientID(), "Extra", 2.0);

        // Act
        OrderItem orderItem = new OrderItem(validProduct, Collections.singletonList(validExtra));

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
        assertThatThrownBy(() -> new OrderItem(new Product(null, null, 0.0)))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Domain validation failed: value Name cannot be null or empty");
    }

    @Test
    void shouldThrowException_whenExtrasContainInvalidData() {
        // Arrange
        Product validProduct = new Product(new ProductID(), "Product", 10.0);

        // Act & Assert
        assertThatThrownBy(() -> new OrderItem(validProduct, List.of(new Extra(null, null, 0.0))))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Domain validation failed: value Name cannot be null or empty");
    }
}
