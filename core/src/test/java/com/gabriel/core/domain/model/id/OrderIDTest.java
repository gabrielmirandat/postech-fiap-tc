package com.gabriel.core.domain.model.id;

import com.gabriel.core.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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

    @Test
    void shouldCompareOrderIDsBasedOnId() {
        OrderID orderID1 = new OrderID("12345678-ORDR-2023-04-18");
        OrderID orderID2 = new OrderID("12345678-ORDR-2023-04-18");
        OrderID orderID3 = new OrderID("87654321-ORDR-2023-04-18");
        assertThat(orderID1).isEqualTo(orderID2);
        assertThat(orderID1).isNotEqualTo(orderID3);
    }
}

