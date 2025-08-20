package com.gabriel.orders.core.application;

import com.gabriel.model.Address;
import com.gabriel.model.Cpf;
import com.gabriel.model.Contact;
import com.gabriel.model.ContactType;
import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.domain.model.OrderItemRef;

import java.util.List;

public class CreateOrderCommandMock {

    public static CreateOrderCommand validCommand(ProductId productId, IngredientId ingredientId) {
        Cpf customer = Cpf.newBuilder().setValue("171.374.500-32").build();
        Address shippingAddress = Address.newBuilder()
            .setStreet("street")
            .setCity("city")
            .setState("ST")
            .setZip("00000-000")
            .build();
        Contact notification = Contact.newBuilder()
            .setType(ContactType.CUSTOM)
            .setCustomValue("blah|blah")
            .build();
        OrderItemRef orderItemRef = new OrderItemRef(productId.getValue(), List.of(ingredientId.getValue()));

        return new CreateOrderCommand(customer, shippingAddress, notification, List.of(orderItemRef));
    }
}
