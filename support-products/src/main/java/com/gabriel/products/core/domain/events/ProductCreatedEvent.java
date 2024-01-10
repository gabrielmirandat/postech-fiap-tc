package com.gabriel.products.core.domain.events;

import com.gabriel.products.core.domain.base.DomainEvent;
import com.gabriel.products.core.domain.entities.Product;
import lombok.Getter;

@Getter
public class ProductCreatedEvent implements DomainEvent {

    Product productAdded;

    public ProductCreatedEvent(Product productAdded) {
        this.productAdded = productAdded;
    }

    @Override
    public String source() {
        return "post/products";
    }

    @Override
    public String subject() {
        return String.format("id/%s", productAdded.getProductID().getId());
    }

    @Override
    public String type() {
        return "postech.menu.v1.product.created";
    }
}
