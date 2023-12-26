package com.gabriel.products.adapter.driven.persistence;

import com.gabriel.products.core.domain.entities.Product;
import com.gabriel.products.core.domain.entities.enums.Category;
import com.gabriel.products.core.domain.ports.ProductRepository;
import com.gabriel.products.core.domain.ports.models.ProductSearchParameters;
import com.gabriel.products.core.domain.valueobjects.Description;
import com.gabriel.products.core.domain.valueobjects.Name;
import com.gabriel.products.core.domain.valueobjects.Price;
import com.gabriel.products.core.domain.valueobjects.ids.IngredientID;
import com.gabriel.products.core.domain.valueobjects.ids.ProductID;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductClientMongoRepository implements ProductRepository {
    @Inject
    private MongoCollection productCollection;

    @Override
    public Product saveProduct(Product product) {
        Document productDoc = ProductConverter.productToDocument(product);
        productCollection.insertOne(productDoc);
        return product;
    }

    @Override
    public Product getById(String id) {
        Document doc = (Document) productCollection.find(Filters.eq("productID", id)).first();
        return doc != null ? ProductConverter.documentToProduct(doc) : null;
    }

    @Override
    public List<Product> searchBy(ProductSearchParameters parameters) {
        List<Product> products = new ArrayList<>();
        if (parameters.category() != null) {
            productCollection.find(Filters.eq("category", parameters.category().toString()))
                .forEach(doc -> products.add(ProductConverter.documentToProduct((Document) doc)));
        }
        return products;
    }

    @Override
    public void deleteProduct(String id) {
        DeleteResult result = productCollection.deleteOne(Filters.eq("productID", id));
        if (result.getDeletedCount() < 1) {
            throw new RuntimeException("Product not found");
        }
    }

    private class ProductConverter {

        public static Document productToDocument(Product product) {
            Document doc = new Document();
            doc.append("productID", product.getProductID().getId())
                .append("name", product.getName().getValue())
                .append("price", product.getPrice().getValue())
                .append("category", product.getCategory().toString())
                .append("description", product.getDescription().getValue());
            List<String> ingredientIds = product.getIngredients().stream()
                .map(IngredientID::getId)
                .collect(Collectors.toList());
            doc.append("ingredients", ingredientIds);
            return doc;
        }

        public static Product documentToProduct(Document doc) {
            ProductID productID = new ProductID(doc.getString("productID"));
            Name name = new Name(doc.getString("name"));
            Price price = new Price(doc.getDouble("price"));
            Category category = Category.valueOf(doc.getString("category").toUpperCase());
            Description description = new Description(doc.getString("description"));
            List<IngredientID> ingredients = ((List<String>) doc.get("ingredients")).stream()
                .map(IngredientID::new)
                .collect(Collectors.toList());
            return new Product(productID, name, price, category, description, ingredients);
        }
    }
}
