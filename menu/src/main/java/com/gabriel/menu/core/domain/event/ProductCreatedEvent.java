package com.gabriel.menu.core.domain.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.menu.core.domain.model.Product;

public class ProductCreatedEvent {

    Product productAdded;

    public ProductCreatedEvent(Product productAdded) {
        this.productAdded = productAdded;
    }

    public String source() {
        return "post/products";
    }

    public String subject() {
        return String.format("id/%s", productAdded.getProductId().getId());
    }

    public String type() {
        return "postech.menu.v1.product.created";
    }

    public byte[] payload(ObjectMapper serializer) {
        return productAdded.serialized(serializer);
    }

    public String audience() {
        return "public";
    }

    public String context() {
        return "menu";
    }

    public Product getProductAdded() {
        return productAdded;
    }
}
