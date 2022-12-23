package ca.ulaval.glo4002.cafe.application.inventory.query;

import java.util.Map;

import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;

public record IngredientsQuery(Map<IngredientType, Quantity> ingredients) {
    public IngredientsQuery(int chocolate, int milk, int water, int espresso) {
        this(Map.of(IngredientType.Chocolate, new Quantity(chocolate), IngredientType.Milk, new Quantity(milk), IngredientType.Water, new Quantity(water),
            IngredientType.Espresso, new Quantity(espresso)));
    }

    public static IngredientsQuery from(int chocolate, int milk, int water, int espresso) {
        return new IngredientsQuery(chocolate, milk, water, espresso);
    }
}
