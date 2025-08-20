package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabriel.orders.core.domain.exception.OrderDomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.gabriel.orders.core.domain.OrderMock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order basicOrder;
    private Order fullOrder;

    @BeforeEach
    void setUp() {
        basicOrder = OrderMock.validBasicOrder();
        fullOrder = OrderMock.validFullOrder();
    }

    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        // Configure to ignore unknown properties (needed for Protobuf objects)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Configure to ignore properties that can't be serialized
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // Configure to ignore properties that cause serialization issues
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        mapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        return mapper;
    }

    @Test
    void testOrderCreationWithoutAdditionalInfo() {
        assertNotNull(basicOrder);
        Assertions.assertEquals(OrderStatus.CREATED, basicOrder.getStatus());
        assertNotNull(basicOrder.getOrderId());
    }

    @Test
    void testOrderCreationWithShippingAddressAndContact() {
        assertNotNull(fullOrder);
        assertEquals(OrderStatus.CREATED, fullOrder.getStatus());
        assertNotNull(fullOrder.getOrderId());
        assertNotNull(fullOrder.getShippingAddress());
        assertNotNull(fullOrder.getContact());
    }

    @Test
    void testGenerateTicket() {
        String expectedTicketId = basicOrder.getOrderId().getValue().split("-")[0];
        assertEquals(expectedTicketId, basicOrder.getTicketId());
    }

    @Test
    void testCalculatePrice() {
        assertNotNull(basicOrder.getPrice());
        assertEquals(22.0, basicOrder.getPrice().getValue());
    }

    @Test
    void testPrepareOrder() {
        basicOrder.promote(OrderStatus.PREPARATION);
        assertEquals(OrderStatus.PREPARATION, basicOrder.getStatus());
    }

    @Test
    void testPackageOrder() {
        basicOrder.promote(OrderStatus.PREPARATION);
        basicOrder.promote(OrderStatus.PACKAGING);
        assertEquals(OrderStatus.PACKAGING, basicOrder.getStatus());
    }

    @Test
    void testPickupOrder() {
        basicOrder.promote(OrderStatus.PREPARATION);
        basicOrder.promote(OrderStatus.PACKAGING);
        basicOrder.promote(OrderStatus.PICKUP);
        assertEquals(OrderStatus.PICKUP, basicOrder.getStatus());
    }

    @Test
    void testDeliverShippableOrder() {
        fullOrder.promote(OrderStatus.PREPARATION);
        fullOrder.promote(OrderStatus.PACKAGING);
        fullOrder.promote(OrderStatus.PICKUP);
        fullOrder.promote(OrderStatus.DELIVERY);
        assertEquals(OrderStatus.DELIVERY, fullOrder.getStatus());
    }

    @Test
    void testDeliverUnshippableOrder() {
        basicOrder.promote(OrderStatus.PREPARATION);
        basicOrder.promote(OrderStatus.PACKAGING);
        basicOrder.promote(OrderStatus.PICKUP);
        assertThrows(OrderDomainException.class, () -> basicOrder.promote(OrderStatus.DELIVERY));
    }

    @Test
    void testFinishShippableOrder() {
        fullOrder.promote(OrderStatus.PREPARATION);
        fullOrder.promote(OrderStatus.PACKAGING);
        fullOrder.promote(OrderStatus.PICKUP);
        fullOrder.promote(OrderStatus.DELIVERY);
        fullOrder.promote(OrderStatus.COMPLETED);
        assertEquals(OrderStatus.COMPLETED, fullOrder.getStatus());
    }

    @Test
    void testFinishUnshippableOrder() {
        basicOrder.promote(OrderStatus.PREPARATION);
        basicOrder.promote(OrderStatus.PACKAGING);
        basicOrder.promote(OrderStatus.PICKUP);
        basicOrder.promote(OrderStatus.COMPLETED);
        assertEquals(OrderStatus.COMPLETED, basicOrder.getStatus());
    }

    @Test
    void testFinishOrderBeforeReady() {
        basicOrder.promote(OrderStatus.PREPARATION);
        basicOrder.promote(OrderStatus.PACKAGING);
        assertThrows(OrderDomainException.class, () -> basicOrder.promote(OrderStatus.COMPLETED));
    }

    @Test
    void testRollbackOrder() {
        fullOrder.promote(OrderStatus.PREPARATION);
        fullOrder.rollback();
        assertEquals(OrderStatus.CREATED, fullOrder.getStatus());
    }

    // @Test
    // void testSerializeOrder() {
    //     byte[] serialized = fullOrder.serialized(objectMapper());
    //     assertThat(serialized).isNotNull();
    // }

    // @Test
    // void testDeserializeOrder() {
    //     byte[] serialized = fullOrder.serialized(objectMapper());
    //     Order deserialized = Order.deserialize(objectMapper(), serialized);
    //     assertThat(deserialized).isNotNull();
    //     assertThat(deserialized.getOrderId()).isEqualTo(fullOrder.getOrderId());
    //     assertThat(deserialized.getShippingAddress().getCity()).isEqualTo(fullOrder.getShippingAddress().getCity());
    //     assertThat(deserialized.getContact().getCustomValue()).isEqualTo(
    //         fullOrder.getContact().getCustomValue());
    //     assertThat(deserialized.getStatus()).isEqualTo(fullOrder.getStatus());
    //     assertThat(deserialized.getPrice().getValue()).isEqualTo(fullOrder.getPrice().getValue());
    //     assertThat(deserialized.getTicketId()).isEqualTo(fullOrder.getTicketId());
    // }
}

