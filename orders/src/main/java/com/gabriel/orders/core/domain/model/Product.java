package com.gabriel.orders.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.domain.ValueObject;
import com.gabriel.domain.model.Name;
import com.gabriel.domain.model.Price;
import com.gabriel.domain.model.id.ProductID;
import lombok.Getter;

import java.io.IOException;
import java.time.Instant;

@Getter
public class Product extends ValueObject {

    private final ProductID productID;

    private final Name name;

    private final Price price;

    private Instant timestamp;

    @JsonCreator
    public Product(ProductID productID, Name name, Price value) {
        this.productID = productID;
        this.name = name;
        this.price = value;
    }

    @JsonCreator
    public Product(ProductID productID, Name name,
                   Price value, @JsonProperty("updateTimestamp") Instant timestamp) {
        this.productID = productID;
        this.name = name;
        this.price = value;
        this.timestamp = timestamp;
    }

    public Product(ProductID productID, String name, Double value) {
        this.productID = productID;
        this.name = new Name(name);
        this.price = new Price(value);
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
            throw new IllegalStateException("Error serializing product");
        }
    }
}
