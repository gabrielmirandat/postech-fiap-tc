package com.gabriel.products.adapter.driven.persistence;

import com.gabriel.common.core.domain.model.Name;
import com.gabriel.common.core.domain.model.Price;
import com.gabriel.common.core.domain.model.id.IngredientID;
import com.gabriel.common.core.domain.model.id.ProductID;
import com.gabriel.products.core.domain.model.Category;
import com.gabriel.products.core.domain.model.Description;
import com.gabriel.products.core.domain.model.Image;
import com.gabriel.products.core.domain.model.Product;
import com.gabriel.products.core.domain.port.ProductRepository;
import com.gabriel.products.core.domain.port.ProductSearchParameters;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductClientMongoRepository implements ProductRepository {

    @Inject
    @Named("productCollection")
    MongoCollection<Document> productCollection;

    @Override
    public Product saveProduct(Product product) {
        Document document = ProductConverter.productToDocument(product);
        productCollection.insertOne(document);
        return product;
    }

    @Override
    public Product getById(String id) {
        Document doc = productCollection.find(Filters.eq("_id", id)).first();
        return doc != null ? ProductConverter.documentToProduct(doc) : null;
    }

    @Override
    public List<Product> searchBy(ProductSearchParameters parameters) {
        List<Product> products = new ArrayList<>();
        if (parameters.category() != null) {
            productCollection.find(Filters.eq("category", parameters.category().toString()))
                .forEach(doc -> products.add(ProductConverter.documentToProduct(doc)));
        }
        return products;
    }

    @Override
    public void deleteProduct(String id) {
        productCollection.deleteOne(Filters.eq("_id", id));
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
            return new Product(productID, name, price, category, description, image, ingredients);
        }
    }
}
