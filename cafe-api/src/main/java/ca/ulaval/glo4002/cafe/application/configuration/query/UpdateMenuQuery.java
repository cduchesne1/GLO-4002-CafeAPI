package ca.ulaval.glo4002.cafe.application.configuration.query;

import java.util.Map;

import ca.ulaval.glo4002.cafe.application.inventory.query.IngredientsQuery;
import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;
import ca.ulaval.glo4002.cafe.domain.ordering.CoffeeName;

public record UpdateMenuQuery(CoffeeName name, Map<IngredientType, Quantity> ingredients, Amount cost) {
    public UpdateMenuQuery(String name, IngredientsQuery ingredients, float cost) {
        this(new CoffeeName(name), ingredients.ingredients(), new Amount(cost));
    }

    public static UpdateMenuQuery from(String name, IngredientsQuery ingredients, float cost) {
        return new UpdateMenuQuery(name, ingredients, cost);
    }
}
