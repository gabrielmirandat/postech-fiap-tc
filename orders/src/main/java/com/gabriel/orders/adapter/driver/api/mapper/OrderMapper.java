package com.gabriel.orders.adapter.driver.api.mapper;

import com.gabriel.domain.model.*;
import com.gabriel.orders.core.application.command.CreateOrderCommand;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderItem;
import com.gabriel.orders.core.domain.model.OrderItemRef;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.specs.orders.models.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public Order toOrder(CreateOrderCommand command, MenuRepository menuRepository) {
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

    public OrderResponse toResponse(Order order) {
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
            OrderStatusDTO.fromValue(order.getStatus().toString().toLowerCase()),
            BigDecimal.valueOf(order.getPrice().getValue()), responseOrderItems);

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
}
