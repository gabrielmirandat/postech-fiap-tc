package com.gabriel.orders.core.domain.model;

import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.core.domain.model.Address;
import com.gabriel.core.domain.model.CPF;
import com.gabriel.core.domain.model.Notification;
import com.gabriel.core.domain.model.NotificationType;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order basicOrder;
    private Order fullOrder;

    @BeforeEach
    void setUp() {
        basicOrder = generateOrder();
        fullOrder = generateFullOrder();
    }

    String generateRandomString() {
        return new Random().ints(48, 123)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(10)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    Extra generateExtra() {
        IngredientID ingredientID = new IngredientID();
        return new Extra(ingredientID, generateRandomString(), 2.0);
    }

    Product generateProduct() {
        ProductID productID = new ProductID();
        return new Product(productID, generateRandomString(), 10.0);
    }

    OrderItem generateOrderItem(boolean withExtra) {
        Product product = generateProduct();

        if (withExtra) {
            Extra extra = generateExtra();
            return new OrderItem(product, Collections.singletonList(extra));
        }

        return new OrderItem(product);
    }

    Order generateOrder() {
        OrderItem item1 = generateOrderItem(false);
        OrderItem item2 = generateOrderItem(true);
        return new Order(Arrays.asList(item1, item2));
    }

    Order generateFullOrder() {
        OrderItem item1 = generateOrderItem(false);
        OrderItem item2 = generateOrderItem(true);

        Address shippingAddress = new Address("Street", "City", "SP", "13011-300");
        Notification notification = new Notification(NotificationType.CUSTOM, "firebase|uuid");
        CPF customer = new CPF("123.456.789-00");
        return new Order(Arrays.asList(item1, item2), customer, shippingAddress, notification);
    }

    @Test
    void testOrderCreationWithoutAdditionalInfo() {
        assertNotNull(basicOrder);
        assertEquals(OrderStatus.CREATED, basicOrder.getStatus());
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
}

