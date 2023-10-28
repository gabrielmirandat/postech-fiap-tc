package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.Entity;
import com.gabriel.orders.core.domain.valueobjects.OrderID;

import java.util.List;

public class OrderItem extends Entity {

    private final OrderID orderItemID;

    private final Product product;

    private final List<Ingredient> extras;
}
