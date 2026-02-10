package com.gabriel.orders.adapter.driver.api.mapper;

import com.gabriel.orders.infra.http.HttpException;
import com.gabriel.model.*;
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
        Cpf customer = null;
        Address shippingAddress = null;
        // Contact omitted at request mapping level if not present in spec
        Contact notification = null;


        if (request.getCustomer() != null) {
            customer = Cpf.newBuilder().setValue(request.getCustomer().getCpf()).build();
        }

        if (request.getShippingAddress() != null) {
            shippingAddress = Address.newBuilder()
                .setStreet(request.getShippingAddress().getStreet())
                .setCity(request.getShippingAddress().getCity())
                .setState(request.getShippingAddress().getState())
                .setZip(request.getShippingAddress().getZip())
                .build();
        }

        // Contact mapping with new proto structure
        if (request.getContact() != null) {
            notification = Contact.newBuilder()
                .setType(ContactType.CELLPHONE)
                .setCellphone(Cellphone.newBuilder().setValue(request.getContact()).build())
                .build();
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
            OrderItem orderItem = OrderItem.create(
                menuRepository.getProduct(item.getProductId()),
                item.getExtrasIds().stream().map(
                        menuRepository::getExtra)
                    .collect(Collectors.toList()));
            orderItems.add(orderItem);
        }

        return Order.create(orderItems, command.customer(), command.shippingAddress(),
            command.notification());
    }

    public static OrderResponse toResponse(Order order) {
        List<OrderItemResponse> responseOrderItems = new ArrayList<>();

        for (var item : order.getItems()) {

            ProductResponse responseProduct = new ProductResponse(
                item.getProduct().getProductId().getValue(),
                item.getProduct().getName().getValue(),
                item.getProduct().getPrice().getValue());

            OrderItemResponse responseOrderItem =
                new OrderItemResponse(item.getItemID().getValue(), responseProduct);

            if (item.getExtras() != null) {
                List<OrderExtraResponse> responseExtras;

                Map<String, Extra> ingredientMap = item.getExtras().stream()
                    .collect(Collectors.toMap(
                        extra -> extra.getIngredientId().getValue(), // Key Mapper
                        extra -> extra,         // Value Mapper
                        (existing, latest) -> latest)); // Merge function, in case of key collision

                Map<String, Integer> ingredientCount = item.getExtras().stream()
                    .collect(Collectors.groupingBy(extra -> extra.getIngredientId().getValue(),
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

        OrderResponse response = new OrderResponse(order.getOrderIdString(), order.getTicketId(),
            OrderStatusDTO.fromValue(order.getStatus().toString().toUpperCase()),
            order.getPriceValue(), responseOrderItems);

        if (order.getCustomerString() != null) {
            response.setCustomer(new CustomerDTO(order.getCustomerString()));
        }

        Order.AddressDto addressDto = order.getShippingAddressDto();
        if (addressDto != null) {
            response.setShippingAddress(new AddressDTO().street(addressDto.street).city(addressDto.city)
                .state(addressDto.state).zip(addressDto.zip));
        }

        Order.ContactDto contactDto = order.getContactDto();
        if (contactDto != null) {
            if (contactDto.cellphone != null) {
                response.setContact(contactDto.cellphone);
            } else if (contactDto.email != null) {
                response.setContact(contactDto.email);
            } else if (contactDto.customValue != null) {
                response.setContact(contactDto.customValue);
            }
        }

        return response;
    }

    public static List<OrderResponse> toResponseList(List<Order> orders) {
        return orders.stream().map(OrderMapper::toResponse).collect(Collectors.toList());
    }


    public static ErrorResponse toErrorResponse(HttpException exception) {
        return toErrorResponse(exception, "", null);
    }
    
    public static ErrorResponse toErrorResponse(HttpException exception, String path) {
        return toErrorResponse(exception, path, null);
    }

    public static ErrorResponse toErrorResponse(HttpException exception, String path, String code) {
        // Represent timestamp as ISO-8601 string; the OpenAPI contract treats it as plain string.
        String timestamp = java.time.OffsetDateTime.now().toString();

        return new ErrorResponse()
            .status(exception.getStatus().value())
            .message(exception.getMessage())
            .code(code != null ? code : "")
            .timestamp(timestamp)
            .error(exception.getStatus().getReasonPhrase())
            .path(path != null ? path : "");
    }

    public static Order toOrder(OrderResponse orderResponse) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemResponse itemResponse : orderResponse.getItems()) {
            Product product = Product.create(
                ProductId.newBuilder().setValue(itemResponse.getProduct().getId()).build(),
                Name.newBuilder().setValue(itemResponse.getProduct().getName()).build(),
                Price.newBuilder().setValue(itemResponse.getProduct().getPrice()).build()
            );

            List<Extra> extras = null;
            if (itemResponse.getExtras() != null) {
                extras = itemResponse.getExtras().stream().map(extraResponse -> Extra.create(
                    IngredientId.newBuilder().setValue(extraResponse.getIngredient().getId()).build(),
                    Name.newBuilder().setValue(extraResponse.getIngredient().getName()).build(),
                    Price.newBuilder().setValue(extraResponse.getIngredient().getPrice()).build()
                )).collect(Collectors.toList());
            }

            OrderItem orderItem = OrderItem.copy(
                OrderItemId.newBuilder().setValue(itemResponse.getItemId()).build(),
                product,
                extras
            );

            orderItems.add(orderItem);
        }

        Cpf customer = null;
        if (orderResponse.getCustomer() != null) {
            customer = Cpf.newBuilder().setValue(orderResponse.getCustomer().getCpf()).build();
        }

        Address shippingAddress = null;
        if (orderResponse.getShippingAddress() != null) {
            shippingAddress = Address.newBuilder()
                .setStreet(orderResponse.getShippingAddress().getStreet())
                .setCity(orderResponse.getShippingAddress().getCity())
                .setState(orderResponse.getShippingAddress().getState())
                .setZip(orderResponse.getShippingAddress().getZip())
                .build();
        }

        Contact notification = null;
        if (orderResponse.getContact() != null) {
            notification = Contact.newBuilder()
                .setType(ContactType.CELLPHONE)
                .setCellphone(Cellphone.newBuilder().setValue(orderResponse.getContact()).build())
                .build();
        }

        return Order.copy(
            OrderId.newBuilder().setValue(orderResponse.getId()).build(),
            orderItems,
            customer,
            shippingAddress,
            notification,
            Price.newBuilder().setValue(orderResponse.getPrice()).build(),
            orderResponse.getTicketId(),
            OrderStatus.valueOf(orderResponse.getStatus().toString().toUpperCase()),
            null,
            null
        );
    }
}
