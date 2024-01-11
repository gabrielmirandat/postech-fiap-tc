package com.gabriel.orders.core.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gabriel.orders.core.domain.base.ValueObject;
import com.gabriel.orders.core.domain.valueobjects.ids.ProductID;
import lombok.Getter;

@Getter
public class Product extends ValueObject {

    private final ProductID productID;

    private final Name name;

    private final Price price;

    @JsonCreator
    public Product(ProductID productID, Name name, Price value) {
        this.productID = productID;
        this.name = name;
        this.price = value;
    }

    public Product(ProductID productID, String name, Double value) {
        this.productID = productID;
        this.name = new Name(name);
        this.price = new Price(value);
    }
}
