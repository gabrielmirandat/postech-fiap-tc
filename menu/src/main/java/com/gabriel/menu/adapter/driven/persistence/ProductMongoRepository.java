package com.gabriel.menu.adapter.driven.persistence;

import com.gabriel.adapter.api.exceptions.NotFound;
import com.gabriel.core.application.exception.ApplicationError;
import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.core.domain.model.Name;
import com.gabriel.core.domain.model.Price;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Description;
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
            throw new ApplicationException(ex.getError().getMessage(), ApplicationError.APP_OO1);
        }
        return product;
    }

    @Override
    public Product getById(ProductID id) {
        Document doc = productCollection.find(Filters.eq("_id", id.getId())).first();

        if (doc != null) {
            return ProductConverter.documentToProduct(doc);
        }
        throw new NotFound("Product not found");
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
    public void deleteProduct(ProductID id) {
        productCollection.deleteOne(Filters.eq("_id", id.getId()));
    }

    private static class ProductConverter {

        public static Document productToDocument(Product product) {
            Document doc = new Document();
            doc.append("_id", product.getProductID().getId())
                .append("name", product.getName().getValue())
                .append("price", product.getPrice().getValue())
                .append("category", product.getCategory().toString())
                .append("description", product.getDescription().getValue())
                .append("image", product.getImage().getUrl());
            List<String> ingredientIds = product.getIngredients().stream()
                .map(IngredientID::getId)
                .collect(Collectors.toList());
            doc.append("ingredients", ingredientIds);
            doc.append("creationTimestamp", product.getCreationTimestamp().toString());
            doc.append("updateTimestamp", product.getUpdateTimestamp().toString());
            return doc;
        }

        public static Product documentToProduct(Document doc) {
            ProductID productID = new ProductID(doc.getString("_id"));
            Name name = new Name(doc.getString("name"));
            Price price = new Price(doc.getDouble("price"));
            Category category = Category.valueOf(doc.getString("category").toUpperCase());
            Description description = new Description(doc.getString("description"));
            Image image = new Image(doc.getString("image"));
            List<IngredientID> ingredients = ((List<String>) doc.get("ingredients")).stream()
                .map(IngredientID::new)
                .collect(Collectors.toList());
            Instant createdAt = Instant.parse(doc.getString("creationTimestamp"));
            Instant updatedAt = Instant.parse(doc.getString("updateTimestamp"));
            return Product.copy(productID, name, price, category, description, image, ingredients,
                createdAt, updatedAt);
        }
    }
}
