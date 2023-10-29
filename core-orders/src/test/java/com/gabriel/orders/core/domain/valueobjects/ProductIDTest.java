package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThat;

class ProductIDTest {

    @Test
    void shouldThrowExceptionWhenIdDoesNotFollowProductIDPattern() {
        assertThatThrownBy(() -> new ProductID("invalid-id"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invalid Product ID format");
    }

    @Test
    void shouldCreateProductIDWhenIdIsValid() {
        String validId = "12345678-PRDC-2023-04-18";
        assertThatCode(() -> new ProductID(validId)).doesNotThrowAnyException();
    }

    @Test
    void shouldGenerateValidProductIDWhenNoIdProvided() {
        ProductID productID = new ProductID();
        String[] parts = productID.getId().split("-");
        assertThat(parts).hasSize(5);
        assertThat(parts[0]).matches("[0-9a-f]{8}");
        assertThat(parts[1]).isEqualTo("PRDC");
        assertThat(parts[2]).matches("\\d{4}");
        assertThat(parts[3]).matches("\\d{2}");
        assertThat(parts[4]).matches("\\d{2}");
    }
}

