package com.gabriel.menu.adapter.driven.persistence;

import com.gabriel.domain.model.Name;
import com.gabriel.domain.model.Price;
import com.gabriel.domain.model.id.IngredientID;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.model.Weight;
import com.gabriel.menu.core.domain.port.IngredientRepository;
import com.gabriel.menu.core.domain.port.ProductSearchParameters;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.bson.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class IngredientClientMongoRepository implements IngredientRepository {

    @Inject
    @Named("ingredientCollection")
    MongoCollection<Document> ingredientCollection;

    @Override
    public Ingredient saveIngredient(Ingredient ingredient) {
        Document document = IngredientConverter.ingredientToDocument(ingredient);
        ingredientCollection.insertOne(document);
        return ingredient;
    }

    @Override
    public Ingredient getById(String id) {
        Document doc = ingredientCollection.find(Filters.eq("_id", id)).first();
        return doc != null ? IngredientConverter.documentToIngredient(doc) : null;
    }

    @Override
    public List<Ingredient> searchBy(ProductSearchParameters parameters) {
        List<Ingredient> ingredients = new ArrayList<>();
        if (parameters.category() != null) {
            ingredientCollection.find(Filters.eq("category", parameters.category().toString()))
                .forEach(doc -> ingredients.add(IngredientConverter.documentToIngredient(doc)));
        }
        return ingredients;
    }

    @Override
    public void deleteIngredient(String id) {
        ingredientCollection.deleteOne(Filters.eq("_id", id));
    }

    private static class IngredientConverter {

        public static Document ingredientToDocument(Ingredient ingredient) {
            Document doc = new Document();
            doc.append("_id", ingredient.getIngredientID().getId())
                .append("name", ingredient.getName().getValue())
                .append("category", ingredient.getCategory().toString())
                .append("price", ingredient.getPrice().getValue())
                .append("weight", ingredient.getWeight().getValue())
                .append("isExtra", ingredient.isExtra())
                .append("creationTimestamp", ingredient.getCreationTimestamp().toString())
                .append("updateTimestamp", ingredient.getUpdateTimestamp().toString());
            return doc;
        }

        public static Ingredient documentToIngredient(Document doc) {
            IngredientID ingredientID = new IngredientID(doc.getString("_id"));
            Name name = new Name(doc.getString("name"));
            Category category = Category.valueOf(doc.getString("category").toUpperCase());
            Price price = new Price(doc.getDouble("price"));
            Weight weight = new Weight(doc.getDouble("weight"));
            boolean isExtra = doc.getBoolean("isExtra");
            Instant createdAt = Instant.parse(doc.getString("creationTimestamp"));
            Instant updatedAt = Instant.parse(doc.getString("updateTimestamp"));

            return Ingredient.copy(ingredientID, name, category, price, weight,
                isExtra, createdAt, updatedAt);
        }
    }
}