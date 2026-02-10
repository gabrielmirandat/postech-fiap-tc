package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // Private constructor for internal use
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

    // Constructor for Jackson deserialization (no validation to avoid duplication)
    @JsonCreator
    public static Product fromJson(@JsonProperty("productId") String productIdStr,
                                   @JsonProperty("name") String nameStr,
                                   @JsonProperty("price") Double valueDouble,
                                   @JsonProperty("timestamp") @JsonAlias("updateTimestamp") Instant timestamp) {
        ProductId productId = ProductId.newBuilder().setValue(productIdStr).build();
        Name name = Name.newBuilder().setValue(nameStr).build();
        Price value = Price.newBuilder().setValue(valueDouble).build();
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

    @JsonIgnore
    public ProductId getProductId() {
        return productId;
    }
    
    @JsonProperty("productId")
    public String getProductIdString() {
        return productId.getValue();
    }

    @JsonIgnore
    public Name getName() {
        return name;
    }
    
    @JsonProperty("name")
    public String getNameString() {
        return name.getValue();
    }

    @JsonIgnore
    public Price getPrice() {
        return price;
    }
    
    @JsonProperty("price")
    public Double getPriceValue() {
        return price.getValue();
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
