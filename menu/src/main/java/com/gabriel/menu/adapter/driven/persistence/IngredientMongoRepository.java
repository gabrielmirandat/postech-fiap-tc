package com.gabriel.menu.adapter.driven.persistence;

import com.gabriel.adapter.api.exceptions.NotFound;
import com.gabriel.core.application.exception.ApplicationError;
import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.core.domain.model.Name;
import com.gabriel.core.domain.model.Price;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.model.Weight;
import com.gabriel.menu.core.domain.port.IngredientRepository;
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

@ApplicationScoped
public class IngredientMongoRepository implements IngredientRepository {

    @Inject
    @Named("ingredientCollection")
    MongoCollection<Document> ingredientCollection;

    @Override
    public Ingredient saveIngredient(Ingredient ingredient) {
        Document document = IngredientConverter.ingredientToDocument(ingredient);

        try {
            ingredientCollection.insertOne(document);
        } catch (MongoWriteException ex) {
            throw new ApplicationException(ex.getError().getMessage(), ApplicationError.APP_OO1);
        }
        return ingredient;
    }

    @Override
    public Ingredient getById(IngredientID id) {
        Document doc = ingredientCollection.find(Filters.eq("_id", id.getId())).first();
        if (doc != null) {
            return IngredientConverter.documentToIngredient(doc);
        }
        throw new NotFound("Ingredient not found");
    }

    @Override
    public List<Ingredient> searchBy(SearchParameters parameters) {
        List<Ingredient> ingredients = new ArrayList<>();
        if (parameters.category() != null) {
            ingredientCollection.find(Filters.eq("category", parameters.category().toString()))
                .forEach(doc -> ingredients.add(IngredientConverter.documentToIngredient(doc)));
        }
        return ingredients;
    }

    @Override
    public void deleteIngredient(IngredientID id) {

        ingredientCollection.deleteOne(Filters.eq("_id", id.getId()));
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
