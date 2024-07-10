package com.gabriel.orders.adapter.driver.api.mapper;

import com.gabriel.adapter.api.exceptions.BaseHttpException;
import com.gabriel.core.domain.model.*;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.OrderID;
import com.gabriel.core.domain.model.id.OrderItemID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.domain.model.*;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.specs.orders.models.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrderMapper {

    public static CreateOrderCommand toCommand(OrderRequest request) {
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
            if (in.getExtras() != null) {
                in.getExtras().forEach(extra -> {
                    for (int i = 0; i < extra.getQuantity(); i++) {
                        extras.add(extra.getIngredientId());
                    }
                });
            }
            return new OrderItemRef(in.getProductId(), extras);
        })).collect(Collectors.toList());

        return new CreateOrderCommand(customer, shippingAddress, notification, items);
    }

    public static Order toOrder(CreateOrderCommand command, MenuRepository menuRepository) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (var item : command.items()) {
            OrderItem orderItem = new OrderItem(
                menuRepository.getProduct(item.getProductId()),
                item.getExtrasIds().stream().map(
                        menuRepository::getExtra)
                    .collect(Collectors.toList()));
            orderItems.add(orderItem);
        }

        return new Order(orderItems, command.customer(), command.shippingAddress(),
            command.notification());
    }

    public static OrderResponse toResponse(Order order) {
        List<OrderItemResponse> responseOrderItems = new ArrayList<>();

        for (var item : order.getItems()) {

            ProductResponse responseProduct = new ProductResponse(
                item.getProduct().getProductID().getId(),
                item.getProduct().getName().getValue(),
                item.getProduct().getPrice().getValue());

            OrderItemResponse responseOrderItem =
                new OrderItemResponse(item.getItemID().getId(), responseProduct);

            if (item.getExtras() != null) {
                List<OrderExtraResponse> responseExtras;

                Map<String, Extra> ingredientMap = item.getExtras().stream()
                    .collect(Collectors.toMap(
                        extra -> extra.getIngredientID().getId(), // Key Mapper
                        extra -> extra,         // Value Mapper
                        (existing, latest) -> latest)); // Merge function, in case of key collision

                Map<String, Integer> ingredientCount = item.getExtras().stream()
                    .collect(Collectors.groupingBy(extra -> extra.getIngredientID().getId(),
                        Collectors.summingInt(e -> 1)));

                responseExtras = ingredientCount.entrySet().stream()
                    .map(entry -> new OrderExtraResponse(
                        new IngredientResponse(entry.getKey(),
                            ingredientMap.get(entry.getKey()).getName().getValue(),
                            ingredientMap.get(entry.getKey()).getPrice().getValue()),
                        BigDecimal.valueOf(entry.getValue()))
                    ).toList();

                responseOrderItem.setExtras(responseExtras);
            }
            responseOrderItems.add(responseOrderItem);
        }

        OrderResponse response = new OrderResponse(order.getOrderId().getId(), order.getTicketId(),
            OrderStatusDTO.fromValue(order.getStatus().toString().toUpperCase()),
            Double.valueOf(order.getPrice().getValue()), responseOrderItems);

        if (order.getCustomer() != null) {
            response.setCustomer(new CustomerDTO(order.getCustomer().getId()));
        }

        if (order.getShippingAddress() != null) {
            response.setShippingAddress(new AddressDTO().street(order.getShippingAddress().getStreet()).city(order.getShippingAddress().getCity())
                .state(order.getShippingAddress().getState()).zip(order.getShippingAddress().getZip()));
        }

        if (order.getNotification() != null) {
            Cellphone phone = (Cellphone) order.getNotification().getRepr();
            response.setNotification(phone.getNumber());
        }

        return response;
    }

    public static List<OrderResponse> toResponseList(List<Order> orders) {
        return orders.stream().map(OrderMapper::toResponse).collect(Collectors.toList());
    }


    public static ErrorResponse toErrorResponse(BaseHttpException exception) {
        return new ErrorResponse()
            .status(exception.getStatus())
            .message(exception.getMessage())
            .code(exception.getCode() != null ? exception.getCode() : "");
    }

    public static Order toOrder(OrderResponse orderResponse) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemResponse itemResponse : orderResponse.getItems()) {
            Product product = new Product(
                new ProductID(itemResponse.getProduct().getId()),
                new Name(itemResponse.getProduct().getName()),
                new Price(itemResponse.getProduct().getPrice())
            );

            List<Extra> extras = null;
            if (itemResponse.getExtras() != null) {
                extras = itemResponse.getExtras().stream().map(extraResponse -> new Extra(
                    new IngredientID(extraResponse.getIngredient().getId()),
                    new Name(extraResponse.getIngredient().getName()),
                    new Price(extraResponse.getIngredient().getPrice())
                )).collect(Collectors.toList());
            }

            OrderItem orderItem = OrderItem.copy(
                new OrderItemID(itemResponse.getItemId()),
                product,
                extras
            );

            orderItems.add(orderItem);
        }

        CPF customer = null;
        if (orderResponse.getCustomer() != null) {
            customer = new CPF(orderResponse.getCustomer().getCpf());
        }

        Address shippingAddress = null;
        if (orderResponse.getShippingAddress() != null) {
            shippingAddress = new Address(
                orderResponse.getShippingAddress().getStreet(),
                orderResponse.getShippingAddress().getCity(),
                orderResponse.getShippingAddress().getState(),
                orderResponse.getShippingAddress().getZip()
            );
        }

        Notification notification = null;
        if (orderResponse.getNotification() != null) {
            notification = new Notification(NotificationType.CELLPHONE, orderResponse.getNotification());
        }

        return Order.copy(
            new OrderID(orderResponse.getId()),
            orderItems,
            customer,
            shippingAddress,
            notification,
            new Price(orderResponse.getPrice()),
            orderResponse.getTicketId(),
            OrderStatus.valueOf(orderResponse.getStatus().toString().toUpperCase()),
            null,
            null
        );
    }
}
