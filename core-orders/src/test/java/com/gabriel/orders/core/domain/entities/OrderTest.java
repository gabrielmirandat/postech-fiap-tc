package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.entities.enums.OrderStatus;
import com.gabriel.orders.core.domain.valueobjects.Address;
import com.gabriel.orders.core.domain.valueobjects.Notification;
import com.gabriel.orders.core.domain.valueobjects.enums.NotificationType;
import com.gabriel.orders.core.domain.valueobjects.ids.IngredientID;
import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order basicOrder;

    private Order fullOrder;

    @BeforeEach
    void setUp() {
        basicOrder = generateOrder();
        fullOrder = generateFullOrder();
    }

    Extra generateExtra() {
        IngredientID ingredientID = new IngredientID();
        return new Extra(ingredientID, 2.0);
    }

    Product generateProduct() {
        ProductID productID = new ProductID();
        return new Product(productID, 10.0);
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
        return new Order(Arrays.asList(item1, item2), shippingAddress, notification);
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
        // TODO: fix time here and check manual
        basicOrder.generateTicket();
        String expectedTicketId = basicOrder.getOrderId().getId().split("-")[0];
        assertEquals(expectedTicketId, basicOrder.getTicketId());
    }

    @Test
    @Disabled
    void testCalculatePrice() {
        basicOrder.calculatePrice();
        assertNotNull(basicOrder.getPrice());
        assertEquals(22.0, basicOrder.getPrice().getValue());
    }

    @Test
    void testPrepareOrder() {
        basicOrder.prepare_order();
        assertEquals(OrderStatus.PREPARATION, basicOrder.getStatus());
    }

    @Test
    void testPackageOrder() {
        basicOrder.prepare_order();
        basicOrder.package_order();
        assertEquals(OrderStatus.PACKAGING, basicOrder.getStatus());
    }

    @Test
    void testPickupOrder() {
        basicOrder.prepare_order();
        basicOrder.package_order();
        basicOrder.pickup_order();
        assertEquals(OrderStatus.PICKUP, basicOrder.getStatus());
    }

    @Test
    void testDeliverOrder() {
        basicOrder.prepare_order();
        basicOrder.package_order();
        basicOrder.pickup_order();
        basicOrder.deliver_order();
        assertEquals(OrderStatus.DELIVERY, basicOrder.getStatus());
    }

    @Test
    void testFinishOrder() {
        basicOrder.prepare_order();
        basicOrder.package_order();
        basicOrder.pickup_order();
        basicOrder.deliver_order();
        basicOrder.finish_order();
        assertEquals(OrderStatus.COMPLETED, basicOrder.getStatus());
    }
}

