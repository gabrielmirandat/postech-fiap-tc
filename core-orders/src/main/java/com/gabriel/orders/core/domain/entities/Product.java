package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.base.Entity;
import com.gabriel.orders.core.domain.valueobjects.*;

import java.util.List;

public class Product extends Entity {

    private final ProductID productID;

    private final Price price;
}
