package com.gabriel.domain.model.id;

import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.core.domain.model.id.ProductID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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

    @Test
    void shouldCompareProductIDsBasedOnId() {
        ProductID productID1 = new ProductID("12345678-PRDC-2023-04-18");
        ProductID productID2 = new ProductID("12345678-PRDC-2023-04-18");
        ProductID productID3 = new ProductID("87654321-PRDC-2023-04-18");
        assertThat(productID1).isEqualTo(productID2);
        assertThat(productID1).isNotEqualTo(productID3);
    }
}

