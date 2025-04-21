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
        Cpf customer = new Cpf("171.374.500-32");
        Address shippingAddress = new Address("street", "city", "ST", "00000-000");
        Contact notification = new Contact(ContactType.CUSTOM, "blah|blah");
        OrderItemRef orderItemRef = new OrderItemRef(productId.getId(), List.of(ingredientId.getId()));

        return new CreateOrderCommand(customer, shippingAddress, notification, List.of(orderItemRef));
    }
}
