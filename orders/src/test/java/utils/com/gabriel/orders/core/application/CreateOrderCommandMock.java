package utils.com.gabriel.orders.core.application;

import com.gabriel.core.domain.model.Address;
import com.gabriel.core.domain.model.CPF;
import com.gabriel.core.domain.model.Notification;
import com.gabriel.core.domain.model.NotificationType;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.domain.model.OrderItemRef;

import java.util.List;

public class CreateOrderCommandMock {

    public static CreateOrderCommand validCommand(ProductID productId, IngredientID ingredientId) {
        CPF customer = new CPF("171.374.500-32");
        Address shippingAddress = new Address("street", "city", "ST", "00000-000");
        Notification notification = new Notification(NotificationType.CUSTOM, "blah|blah");
        OrderItemRef orderItemRef = new OrderItemRef(productId.getId(), List.of(ingredientId.getId()));

        return new CreateOrderCommand(customer, shippingAddress, notification, List.of(orderItemRef));
    }
}
