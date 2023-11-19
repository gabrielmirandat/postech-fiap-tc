package com.gabriel.orders.adapter.driver.api.mappers;

import com.gabriel.orders.adapter.driver.api.controllers.models.OrderItemResponse;
import com.gabriel.orders.adapter.driver.api.controllers.models.OrderRequest;
import com.gabriel.orders.adapter.driver.api.controllers.models.OrderResponse;
import com.gabriel.orders.core.application.commands.CreateOrderCommand;
import com.gabriel.orders.core.application.queries.GetByTicketOrderQuery;
import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.valueobjects.Address;
import com.gabriel.orders.core.domain.valueobjects.CPF;
import com.gabriel.orders.core.domain.valueobjects.Notification;
import com.gabriel.orders.core.domain.valueobjects.OrderItemRef;
import com.gabriel.orders.core.domain.valueobjects.enums.NotificationType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class OrderMapper {

    public CreateOrderCommand toCommand(OrderRequest request) {
        CPF customer = null;
        Address shippingAddress = null;
        Notification notification = null;


        if (request.getCustomer() != null) {
            customer = new CPF(request.getCustomer().getCpf());
        }

        if (request.getShippingAddress() != null) {
            shippingAddress = new Address(request.getShippingAddress().getStreet(), request.getShippingAddress().getCity(), request.getShippingAddress().getState(), request.getShippingAddress().getZip());
        }

        if (request.getNotification() != null) {
            notification = new Notification(NotificationType.CELLPHONE, request.getNotification());
        }

        List<OrderItemRef> items = request.getItems().stream().flatMap(in -> IntStream.range(0, in.getQuantity()).mapToObj(dump -> {
            List<String> extras = new ArrayList<>();
            in.getExtras().forEach(extra -> {
                for (int i = 0; i < extra.getQuantity(); i++) {
                    extras.add(extra.getIngredientId());
                }
            });
            return new OrderItemRef(in.getProductId(), extras);
        })).collect(Collectors.toList());

        return new CreateOrderCommand(customer, shippingAddress, notification, items);
    }

    public GetByTicketOrderQuery toQuery(String orderId) {
        return new GetByTicketOrderQuery(orderId);
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = new ArrayList<>();

        for (var item : order.getItems()) {
            var extras = item.getExtras();

        }

        return new OrderResponse();

    }
}
