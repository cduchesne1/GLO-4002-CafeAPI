package ca.ulaval.glo4002.cafe.application.configuration.query;

import java.util.Map;

import ca.ulaval.glo4002.cafe.api.inventory.request.InventoryRequest;
import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeName;

public record UpdateMenuQuery(CoffeeName name, Map<IngredientType, Quantity> ingredients, Amount cost) {
    public UpdateMenuQuery(String name, InventoryRequest ingredients, float cost) {
        this(new CoffeeName(name),
            Map.of(IngredientType.Chocolate, new Quantity(ingredients.Chocolate), IngredientType.Espresso, new Quantity(ingredients.Espresso),
                IngredientType.Water, new Quantity(ingredients.Water), IngredientType.Milk, new Quantity(ingredients.Milk)), new Amount(cost));
    }

    public static UpdateMenuQuery from(String name, InventoryRequest ingredients, float cost) {
        return new UpdateMenuQuery(name, ingredients, cost);
    }
}
