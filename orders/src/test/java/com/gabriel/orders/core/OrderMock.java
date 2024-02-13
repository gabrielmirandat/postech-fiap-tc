package com.gabriel.orders.core;

import com.gabriel.core.domain.model.Address;
import com.gabriel.core.domain.model.CPF;
import com.gabriel.core.domain.model.Notification;
import com.gabriel.core.domain.model.NotificationType;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.domain.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OrderMock {

    public static OrderItem generateOrderItem(boolean withExtra) {
        Product product = generateProduct();

        if (withExtra) {
            Extra extra = generateExtra();
            return new OrderItem(product, Collections.singletonList(extra));
        }

        return new OrderItem(product);
    }

    public static Order generateBasic() {
        OrderItem item1 = generateOrderItem(false);
        OrderItem item2 = generateOrderItem(true);
        return new Order(Arrays.asList(item1, item2));
    }

    public static Order generateFull() {
        OrderItem item1 = generateOrderItem(false);
        OrderItem item2 = generateOrderItem(true);

        Address shippingAddress = new Address("Street", "City", "SP", "13011-300");
        Notification notification = new Notification(NotificationType.CUSTOM, "firebase|uuid");
        CPF customer = new CPF("123.456.789-00");
        return new Order(Arrays.asList(item1, item2), customer, shippingAddress, notification);
    }

    public static Extra generateExtra() {
        IngredientID ingredientID = new IngredientID("11111111-INGR-1111-11-11");
        return new Extra(ingredientID, generateRandomString(), 2.0);
    }

    public static Product generateProduct() {
        ProductID productID = new ProductID("11111111-PRDC-1111-11-11");
        return new Product(productID, generateRandomString(), 10.0);
    }

    public static String generateRandomString() {
        return new Random().ints(48, 123)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(10)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    public static CreateOrderCommand generateCreateOrderCommand(ProductID productId, IngredientID ingredientId) {
        CPF customer = new CPF("171.374.500-32");
        Address shippingAddress = new Address("street", "city", "ST", "00000-000");
        Notification notification = new Notification(NotificationType.CUSTOM, "blah|blah");
        OrderItemRef orderItemRef = new OrderItemRef(productId.getId(), List.of(ingredientId.getId()));

        return new CreateOrderCommand(customer, shippingAddress, notification, List.of(orderItemRef));
    }
}
