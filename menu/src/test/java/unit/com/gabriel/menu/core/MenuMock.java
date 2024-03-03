package unit.com.gabriel.menu.core;

import com.gabriel.menu.core.domain.model.Category;
import com.gabriel.menu.core.domain.model.Ingredient;
import com.gabriel.menu.core.domain.model.Product;

import java.util.Arrays;
import java.util.List;

public class MenuMock {

    public static Ingredient generateIngredient(boolean isExtra) {
        String name = "anIngredient";
        Category category = Category.BURGER;
        Double price = 1.0;
        Double weight = 1.0;
        return new Ingredient(name, category, price, weight, isExtra);
    }

    public static Product generateProduct() {
        String name = "aProduct";
        Category category = Category.BURGER;
        Double price = 1.0;
        Double weight = 1.0;

        Ingredient ingredient1 = generateIngredient(true);
        Ingredient ingredient2 = generateIngredient(false);
        Ingredient ingredient3 = generateIngredient(true);

        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);
        List<Ingredient> allIngredients = Arrays.asList(ingredient1, ingredient2, ingredient3);

        return new Product(name, price, category, "aDescription",
            "product.png", ingredients.stream().map(Ingredient::getIngredientID).toList(),
            allIngredients.stream().map(Ingredient::getIngredientID).toList());
    }
}
