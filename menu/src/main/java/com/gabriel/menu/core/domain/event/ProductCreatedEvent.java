package com.gabriel.menu.core.domain.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.core.domain.DomainEvent;
import com.gabriel.menu.core.domain.model.Product;

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

    @Override
    public byte[] payload(ObjectMapper serializer) {
        return productAdded.serialized(serializer);
    }

    public Product getProductAdded() {
        return productAdded;
    }
}
