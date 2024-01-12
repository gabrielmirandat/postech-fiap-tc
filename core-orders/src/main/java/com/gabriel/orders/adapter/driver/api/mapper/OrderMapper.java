package com.gabriel.orders.adapter.driver.api.mapper;

import com.gabriel.common.core.domain.model.Address;
import com.gabriel.common.core.domain.model.CPF;
import com.gabriel.common.core.domain.model.Notification;
import com.gabriel.common.core.domain.model.NotificationType;
import com.gabriel.orders.adapter.driver.api.models.*;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.application.query.GetByTicketOrderQuery;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderItemRef;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    // String id, String ticketId, OrderStatusDTO status, BigDecimal price,
    // List<@Valid OrderItemResponse> items
    public OrderResponse toResponse(Order order) {

        List<OrderItemResponse> orderItems = new ArrayList<>();
        for (var item : order.getItems()) {
            var extras = item.getExtras();

            ProductResponse productResponse = new ProductResponse();
            OrderItemResponse orderItem =
                new OrderItemResponse(item.getItemID().toString(), productResponse);

            orderItems.add(orderItem);
        }

        OrderResponse response = new OrderResponse();
        response.setId(order.getOrderId().toString());
        response.setTicketId(order.getTicketId());
        response.setCustomer(null);
        response.setShippingAddress(null);
        response.setPrice(BigDecimal.valueOf(order.getPrice().getValue()));
        response.setNotification(order.getNotification().getValue().toString());
        response.setStatus(OrderStatusDTO.valueOf(order.getStatus().toString()));
        response.setItems(orderItems);

        return response;
    }
}
