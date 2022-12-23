package ca.ulaval.glo4002.cafe.domain.inventory;

import java.util.HashMap;
import java.util.Map;

import ca.ulaval.glo4002.cafe.domain.exception.InsufficientIngredientsException;

public class Inventory {
    private Map<IngredientType, Quantity> ingredients = new HashMap<>();

    public Map<IngredientType, Quantity> getIngredients() {
        return ingredients;
    }

    public void clear() {
        ingredients = new HashMap<>();
    }

    public void add(Map<IngredientType, Quantity> newIngredients) {
        newIngredients.forEach((ingredientType, quantity) -> {
            if (ingredients.containsKey(ingredientType)) {
                ingredients.put(ingredientType, ingredients.get(ingredientType).add(quantity));
            } else {
                ingredients.put(ingredientType, quantity);
            }
        });
    }

    public void useIngredients(Map<IngredientType, Quantity> ingredients) {
        validateIfEnoughIngredients(ingredients);
        removeIngredients(ingredients);
    }

    private void validateIfEnoughIngredients(Map<IngredientType, Quantity> ingredientsNeeded) {
        ingredientsNeeded.forEach((ingredientType, quantity) -> {
            if (!ingredients.containsKey(ingredientType) || quantity.isGreaterThan(ingredients.get(ingredientType))) {
                throw new InsufficientIngredientsException();
            }
        });
    }

    private void removeIngredients(Map<IngredientType, Quantity> ingredientsToRemove) {
        ingredientsToRemove.forEach((ingredientType, quantity) -> {
            ingredients.put(ingredientType, ingredients.get(ingredientType).remove(quantity));
            if (ingredients.get(ingredientType).value() == 0) {
                ingredients.remove(ingredientType);
            }
        });
    }
}
