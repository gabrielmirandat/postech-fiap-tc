package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThat;

class OrderIDTest {

    @Test
    void shouldThrowExceptionWhenIdDoesNotFollowOrderIDPattern() {
        assertThatThrownBy(() -> new OrderID("invalid-id"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Invalid Order ID format");
    }

    @Test
    void shouldCreateOrderIDWhenIdIsValid() {
        String validId = "12345678-ORDR-2023-04-18";
        assertThatCode(() -> new OrderID(validId)).doesNotThrowAnyException();
    }

    @Test
    void shouldGenerateValidOrderIDWhenNoIdProvided() {
        OrderID orderID = new OrderID();
        String[] parts = orderID.getId().split("-");
        assertThat(parts).hasSize(5);
        assertThat(parts[0]).matches("[0-9a-f]{8}");
        assertThat(parts[1]).isEqualTo("ORDR");
        assertThat(parts[2]).matches("\\d{4}");
        assertThat(parts[3]).matches("\\d{2}");
        assertThat(parts[4]).matches("\\d{2}");
    }
}

