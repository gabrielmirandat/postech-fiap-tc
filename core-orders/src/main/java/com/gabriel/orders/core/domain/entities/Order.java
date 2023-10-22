package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.valueobjects.Address;
import com.gabriel.orders.core.domain.valueobjects.Notification;
import com.gabriel.orders.core.domain.valueobjects.OrderID;
import com.gabriel.orders.core.domain.valueobjects.Price;

import java.util.List;

public record Order(OrderID orderId, List<Product> products, Address shippingAddress,
                    Notification additionalNotification, Price price) {

}
