package com.gabriel.products.core.domain.event;


import com.gabriel.common.core.domain.base.DomainEvent;
import com.gabriel.products.core.domain.model.Product;
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
