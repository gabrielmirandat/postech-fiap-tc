package unit.com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unit.com.gabriel.orders.core.OrderMock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order basicOrder;
    private Order fullOrder;

    @BeforeEach
    void setUp() {
        basicOrder = OrderMock.generateBasic();
        fullOrder = OrderMock.generateFull();
    }

    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Test
    void testOrderCreationWithoutAdditionalInfo() {
        assertNotNull(basicOrder);
        Assertions.assertEquals(OrderStatus.CREATED, basicOrder.getStatus());
        assertNotNull(basicOrder.getOrderId());
    }

    @Test
    void testOrderCreationWithShippingAddressAndNotification() {
        assertNotNull(fullOrder);
        assertEquals(OrderStatus.CREATED, fullOrder.getStatus());
        assertNotNull(fullOrder.getOrderId());
        assertNotNull(fullOrder.getShippingAddress());
        assertNotNull(fullOrder.getNotification());
    }

    @Test
    void testGenerateTicket() {
        String expectedTicketId = basicOrder.getOrderId().getId().split("-")[0];
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
        assertThrows(DomainException.class, () -> basicOrder.promote(OrderStatus.DELIVERY));
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
        assertThrows(DomainException.class, () -> basicOrder.promote(OrderStatus.COMPLETED));
    }

    @Test
    void testRollbackOrder() {
        fullOrder.promote(OrderStatus.PREPARATION);
        fullOrder.rollback();
        assertEquals(OrderStatus.CREATED, fullOrder.getStatus());
    }

    @Test
    void testSerializeOrder() {
        byte[] serialized = fullOrder.serialized(objectMapper());
        assertThat(serialized).isNotNull();
    }

    @Test
    void testDeserializeOrder() {
        byte[] serialized = fullOrder.serialized(objectMapper());
        Order deserialized = Order.deserialize(objectMapper(), serialized);
        assertThat(deserialized).isNotNull();
        assertThat(deserialized.getOrderId()).isEqualTo(fullOrder.getOrderId());
        assertThat(deserialized.getShippingAddress().getCity()).isEqualTo(fullOrder.getShippingAddress().getCity());
        assertThat(deserialized.getNotification().getRepr().getValue()).isEqualTo(
            fullOrder.getNotification().getRepr().getValue());
        assertThat(deserialized.getStatus()).isEqualTo(fullOrder.getStatus());
        assertThat(deserialized.getPrice().getValue()).isEqualTo(fullOrder.getPrice().getValue());
        assertThat(deserialized.getTicketId()).isEqualTo(fullOrder.getTicketId());
    }
}

