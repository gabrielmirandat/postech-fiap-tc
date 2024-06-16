package utils.com.gabriel.orders.core.domain;

import com.gabriel.core.domain.model.Address;
import com.gabriel.core.domain.model.CPF;
import com.gabriel.core.domain.model.Notification;
import com.gabriel.core.domain.model.NotificationType;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderItem;

import java.util.Arrays;

import static utils.com.gabriel.orders.core.domain.OrderItemMock.validOrderItem;

public class OrderMock {


    public static Order validBasicOrder() {
        OrderItem item1 = validOrderItem(false);
        OrderItem item2 = validOrderItem(true);
        return new Order(Arrays.asList(item1, item2));
    }

    public static Order validFullOrder() {
        OrderItem item1 = validOrderItem(false);
        OrderItem item2 = validOrderItem(true);

        Address shippingAddress = new Address("Street", "City", "SP", "41710-450");
        Notification notification = new Notification(NotificationType.CELLPHONE, "(19) 12345-5555");
        CPF customer = new CPF("123.456.789-00");
        return new Order(Arrays.asList(item1, item2), customer, shippingAddress, notification);
    }
}
