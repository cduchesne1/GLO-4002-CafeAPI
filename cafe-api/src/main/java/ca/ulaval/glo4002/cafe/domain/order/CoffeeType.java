package ca.ulaval.glo4002.cafe.domain.order;

import java.util.Map;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.exception.InvalidMenuOrderException;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;

public enum CoffeeType {
    Americano("Americano", new Amount(2.25f), Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Water, new Quantity(50))),

    DarkRoast("Dark Roast", new Amount(2.10f),
        Map.of(IngredientType.Espresso, new Quantity(40), IngredientType.Water, new Quantity(40), IngredientType.Chocolate, new Quantity(10),
            IngredientType.Milk, new Quantity(10))),

    Cappuccino("Cappuccino", new Amount(3.29f),
        Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Water, new Quantity(40), IngredientType.Milk, new Quantity(10))),

    Espresso("Espresso", new Amount(2.95f), Map.of(IngredientType.Espresso, new Quantity(60))),

    FlatWhite("Flat White", new Amount(3.75f), Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Milk, new Quantity(50))),

    Latte("Latte", new Amount(2.95f), Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Milk, new Quantity(50))),

    Macchiato("Macchiato", new Amount(4.75f), Map.of(IngredientType.Espresso, new Quantity(80), IngredientType.Milk, new Quantity(20))),

    Mocha("Mocha", new Amount(4.15f),
        Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Milk, new Quantity(40), IngredientType.Chocolate, new Quantity(10)));

    private final String type;
    private final Amount price;
    private final Map<IngredientType, Quantity> ingredients;

    CoffeeType(String type, Amount price, Map<IngredientType, Quantity> ingredients) {
        this.type = type;
        this.price = price;
        this.ingredients = ingredients;
    }

    private static boolean contains(String other) {
        for (CoffeeType coffeeType : CoffeeType.values()) {
            if (coffeeType.type.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public static CoffeeType fromString(String type) {
        if (CoffeeType.contains(type)) {
            return CoffeeType.valueOf(type.replace(" ", ""));
        }
        throw new InvalidMenuOrderException();
    }

    @Override
    public String toString() {
        return type;
    }

    public Amount getPrice() {
        return price;
    }
}
