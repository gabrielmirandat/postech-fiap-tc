package com.gabriel.orders.core;

import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.model.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public class UtilsMock {

    public static String generateRandomString() {
        return new Random().ints(48, 123)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(10)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    public static ProductId generateProductId() {
        String hex = UUID.randomUUID().toString().substring(0, 8).replace("-", "");
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String productIdValue = hex + "-PRDC-" + date;
        return (ProductId) Model.validate(ProductId.newBuilder().setValue(productIdValue).build());
    }

    public static ProductId generateProductId(String value) {
        return (ProductId) Model.validate(ProductId.newBuilder().setValue(value).build());
    }

    public static IngredientId generateIngredientId() {
        String hex = UUID.randomUUID().toString().substring(0, 8).replace("-", "");
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String ingredientIdValue = hex + "-INGR-" + date;
        return (IngredientId) Model.validate(IngredientId.newBuilder().setValue(ingredientIdValue).build());
    }

    public static IngredientId generateIngredientId(String value) {
        return (IngredientId) Model.validate(IngredientId.newBuilder().setValue(value).build());
    }
}
