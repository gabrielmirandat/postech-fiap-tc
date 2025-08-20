package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.model.ProductId;
import com.gabriel.model.Model;

import java.io.IOException;
import java.time.Instant;

public class Product {

    private final ProductId productId;

    private final Name name;

    private final Price price;

    private Instant timestamp;

    // Construtor privado para uso interno
    private Product(ProductId productId, Name name, Price value, Instant timestamp) {
        this.productId = productId;
        this.name = name;
        this.price = value;
        this.timestamp = timestamp;
    }

    // Factory methods que sempre validam
    public static Product create(ProductId productId, String name, Double value) {
        ProductId validatedProductId = (ProductId) Model.validate(productId);
        Name validatedName = (Name) Model.validate(Name.newBuilder().setValue(name).build());
        Price validatedPrice = (Price) Model.validate(Price.newBuilder().setValue(value).build());
        return new Product(validatedProductId, validatedName, validatedPrice, null);
    }

    public static Product create(ProductId productId, Name name, Price value) {
        Name validatedName = (Name) Model.validate(name);
        Price validatedPrice = (Price) Model.validate(value);
        return new Product(productId, validatedName, validatedPrice, null);
    }

    public static Product create(ProductId productId, Name name, Price value, Instant timestamp) {
        Name validatedName = (Name) Model.validate(name);
        Price validatedPrice = (Price) Model.validate(value);
        return new Product(productId, validatedName, validatedPrice, timestamp);
    }

    // Construtor para Jackson deserialization (sem validação para evitar duplicação)
    @JsonCreator
    public static Product fromJson(@JsonProperty("productId") ProductId productId,
                                   @JsonProperty("name") Name name,
                                   @JsonProperty("price") Price value,
                                   @JsonProperty("timestamp") @JsonAlias("updateTimestamp") Instant timestamp) {
        return new Product(productId, name, value, timestamp);
    }

    public static Product deserialize(ObjectMapper deserializer, byte[] bytes) {
        try {
            return deserializer.readValue(bytes, Product.class);
        } catch (IOException e) {
            throw new IllegalStateException("Error deserializing product");
        }
    }

    public byte[] serialized(ObjectMapper serializer) {
        try {
            return serializer.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error serializing product: " + e.getMessage(), e);
        }
    }

    public ProductId getProductId() {
        return productId;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
