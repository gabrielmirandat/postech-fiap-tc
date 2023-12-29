package com.gabriel.products.adapter.driven.persistence;

import com.gabriel.products.core.domain.entities.Ingredient;
import com.gabriel.products.core.domain.entities.enums.Category;
import com.gabriel.products.core.domain.ports.IngredientRepository;
import com.gabriel.products.core.domain.ports.models.ProductSearchParameters;
import com.gabriel.products.core.domain.valueobjects.Name;
import com.gabriel.products.core.domain.valueobjects.Price;
import com.gabriel.products.core.domain.valueobjects.ids.IngredientID;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class IngredientClientMongoRepository implements IngredientRepository {

    @Inject
    @Named("ingredientCollection")
    MongoCollection ingredientCollection;

    @Override
    public Ingredient saveIngredient(Ingredient ingredient) {
        Document document = IngredientConverter.ingredientToDocument(ingredient);
        ingredientCollection.insertOne(document);
        return ingredient;
    }

    @Override
    public Ingredient getById(String id) {
        Document doc = (Document) ingredientCollection.find(Filters.eq("ingredientID", id)).first();
        return doc != null ? IngredientConverter.documentToIngredient(doc) : null;
    }

    @Override
    public List<Ingredient> searchBy(ProductSearchParameters parameters) {
        List<Ingredient> ingredients = new ArrayList<>();
        if (parameters.category() != null) {
            ingredientCollection.find(Filters.eq("category", parameters.category().toString()))
                .forEach(doc -> ingredients.add(IngredientConverter.documentToIngredient((Document) doc)));
        }
        return ingredients;
    }

    @Override
    public void deleteIngredient(String id) {
        DeleteResult result = ingredientCollection.deleteOne(Filters.eq("ingredientID", id));
        if (result.getDeletedCount() < 1) {
            throw new RuntimeException("Ingredient not found");
        }
    }

    private class IngredientConverter {

        public static Document ingredientToDocument(Ingredient ingredient) {
            Document doc = new Document();
            doc.append("ingredientID", ingredient.getIngredientID().getId())
                .append("name", ingredient.getName().getValue())
                .append("price", ingredient.getPrice().getValue())
                .append("category", ingredient.getCategory().toString());
            return doc;
        }

        public static Ingredient documentToIngredient(Document doc) {
            IngredientID ingredientID = new IngredientID(doc.getString("ingredientID"));
            Name name = new Name(doc.getString("name"));
            Price price = new Price(doc.getDouble("price"));
            Category category = Category.valueOf(doc.getString("category").toUpperCase());
            return new Ingredient(ingredientID, name, price, category);
        }
    }
}
