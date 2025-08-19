package com.gabriel.orders.core.domain;

import com.gabriel.model.Address;
import com.gabriel.model.Cpf;
import com.gabriel.model.Contact;
import com.gabriel.model.ContactType;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderItem;
import com.gabriel.model.Cellphone;

import java.util.Arrays;

import static com.gabriel.orders.core.domain.OrderItemMock.validOrderItem;

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
        Contact notification = Contact.newBuilder()
            .setType(ContactType.CELLPHONE)
            .setCellphone(Cellphone.newBuilder().setValue("(19) 12345-5555").build())
            .build();
        Cpf customer = new Cpf("123.456.789-00");
        return new Order(Arrays.asList(item1, item2), customer, shippingAddress, notification);
    }
}
