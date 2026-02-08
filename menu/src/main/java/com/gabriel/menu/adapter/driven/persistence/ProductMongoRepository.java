package com.gabriel.menu.adapter.driven.persistence;

import com.gabriel.model.Description;
import com.gabriel.model.Name;
import com.gabriel.model.Price;
import com.gabriel.model.IngredientId;
import com.gabriel.model.ProductId;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Image;
import com.gabriel.menu.core.domain.model.Product;
import com.gabriel.menu.core.domain.port.ProductRepository;
import com.gabriel.menu.core.domain.port.SearchParameters;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.bson.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductMongoRepository implements ProductRepository {

    @Inject
    @Named("productCollection")
    MongoCollection<Document> productCollection;

    @Override
    public Product saveProduct(Product product) {
        Document document = ProductConverter.productToDocument(product);

        try {
            productCollection.insertOne(document);
        } catch (MongoWriteException ex) {
            throw new RuntimeException("Database error: " + ex.getError().getMessage());
        }
        return product;
    }

    @Override
    public Product getById(ProductId id) {
        Document doc = productCollection.find(Filters.eq("_id", id.getValue())).first();

        if (doc != null) {
            return ProductConverter.documentToProduct(doc);
        }
        throw new RuntimeException("Product not found");
    }

    @Override
    public List<Product> searchBy(SearchParameters parameters) {
        List<Product> products = new ArrayList<>();
        if (parameters.category() != null) {
            productCollection.find(Filters.eq("category", parameters.category().toString()))
                .forEach(doc -> products.add(ProductConverter.documentToProduct(doc)));
        }
        return products;
    }

    @Override
    public void deleteProduct(ProductId id) {
        productCollection.deleteOne(Filters.eq("_id", id.getValue()));
    }

    private static class ProductConverter {

        public static Document productToDocument(Product product) {
            Document doc = new Document();
            doc.append("_id", product.getProductId().getValue())
                .append("name", product.getName().getValue())
                .append("price", product.getPrice().getValue())
                .append("category", product.getCategory().toString())
                .append("description", product.getDescription().getValue())
                .append("image", product.getImage().getUrl());
            List<String> ingredientIds = product.getIngredients().stream()
                .map(IngredientId::getValue)
                .collect(Collectors.toList());
            doc.append("ingredients", ingredientIds);
            doc.append("creationTimestamp", product.getCreationTimestamp().toString());
            doc.append("updateTimestamp", product.getUpdateTimestamp().toString());
            return doc;
        }

        public static Product documentToProduct(Document doc) {
            ProductId productId = ProductId.newBuilder().setValue(doc.getString("_id")).build();
            Name name = Name.newBuilder().setValue(doc.getString("name")).build();
            Price price = Price.newBuilder().setValue(doc.getDouble("price")).build();
            Category category = Category.valueOf(doc.getString("category").toUpperCase());
            Description description = Description.newBuilder().setValue(doc.getString("description")).build();
            Image image = new Image(doc.getString("image"));
            List<IngredientId> ingredients = ((List<String>) doc.get("ingredients")).stream()
                .map(id -> IngredientId.newBuilder().setValue(id).build())
                .collect(Collectors.toList());
            Instant createdAt = Instant.parse(doc.getString("creationTimestamp"));
            Instant updatedAt = Instant.parse(doc.getString("updateTimestamp"));
            return Product.copy(productId, name, price, category, description, image, ingredients,
                createdAt, updatedAt);
        }
    }
}
