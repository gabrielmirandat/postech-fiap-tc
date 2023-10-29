package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.entities.enums.OrderStatus;
import com.gabriel.orders.core.domain.valueobjects.Address;
import com.gabriel.orders.core.domain.valueobjects.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private List<OrderItem> items;
    private Address shippingAddress;
    private Notification notification;

    @BeforeEach
    void setUp() {
        // Inicializar objetos de teste e mockar dependências se necessário
        items = new ArrayList<>();
        // Adicione OrderItems ao 'items' conforme necessário

        // Mocking ou criação de objetos reais
        shippingAddress = Mockito.mock(Address.class);
        notification = Mockito.mock(Notification.class);
    }

    @Test
    void testOrderCreationWithoutAdditionalInfo() {
        Order order = new Order(items);
        assertNotNull(order);
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertNotNull(order.getOrderId());
    }

    @Test
    void testOrderCreationWithShippingAddressAndNotification() {
        Order order = new Order(items, shippingAddress, notification);
        assertNotNull(order);
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertNotNull(order.getOrderId());
        assertNotNull(order.getShippingAddress());
        assertNotNull(order.getNotification());
    }

    @Test
    void testGenerateTicket() {
        Order order = new Order(items);
        order.generateTicket();
        String expectedTicketId = order.getOrderId().getId().split("-")[0];
        assertEquals(expectedTicketId, order.getTicketId());
    }

    @Test
    @Disabled
    void testCalculatePrice() {
        // Adicione os itens e os extras necessários ao 'items' para calcular o preço
        Order order = new Order(items);
        order.calculatePrice();
        assertNotNull(order.getPrice());
    }

    @Test
    void testPrepareOrder() {
        Order order = new Order(items);
        order.prepare_order();
        assertEquals(OrderStatus.PREPARATION, order.getStatus());
    }

    @Test
    void testPackageOrder() {
        Order order = new Order(items);
        order.prepare_order();
        order.package_order();
        assertEquals(OrderStatus.PACKAGING, order.getStatus());
    }

    @Test
    void testPickupOrder() {
        Order order = new Order(items);
        order.prepare_order();
        order.package_order();
        order.pickup_order();
        assertEquals(OrderStatus.PICKUP, order.getStatus());
    }

    @Test
    void testDeliverOrder() {
        Order order = new Order(items, shippingAddress, notification);
        order.prepare_order();
        order.package_order();
        order.pickup_order();
        order.deliver_order();
        assertEquals(OrderStatus.DELIVERY, order.getStatus());
    }

    @Test
    void testFinishOrder() {
        Order order = new Order(items, shippingAddress, notification);
        order.prepare_order();
        order.package_order();
        order.pickup_order();
        order.deliver_order();
        order.finish_order();
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    // Adicione mais testes conforme necessário
}

