package ca.ulaval.glo4002.cafe.application.configuration.query;

import java.util.Map;

import ca.ulaval.glo4002.cafe.api.inventory.request.InventoryRequest;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;

public record UpdateMenuQuery(String name, Map<IngredientType, Quantity> ingredients, float cost) {
    public UpdateMenuQuery(String name, InventoryRequest ingredients, float cost) {
        this(name, Map.of(IngredientType.Chocolate, new Quantity(ingredients.Chocolate), IngredientType.Espresso, new Quantity(ingredients.Espresso),
            IngredientType.Water, new Quantity(ingredients.Water), IngredientType.Milk, new Quantity(ingredients.Milk)), cost);
    }

    public static UpdateMenuQuery from(String name, InventoryRequest ingredients, float cost) {
        return new UpdateMenuQuery(name, ingredients, cost);
    }
}
