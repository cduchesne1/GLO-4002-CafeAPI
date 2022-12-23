package ca.ulaval.glo4002.cafe.domain.menu;

import java.util.Map;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.order.Order;

public class Menu {
    private final Map<CoffeeName, Coffee> coffees;

    public Menu(Map<CoffeeName, Coffee> coffees) {
        this.coffees = coffees;
    }

    public Map<IngredientType, Quantity> getIngredientsNeeded(Order order) {
        return order.items().stream().map(coffees::get).flatMap(coffee -> coffee.ingredients().entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Quantity::add));
    }

    public Amount getItemPrice(CoffeeName coffeeName) {
        return coffees.get(coffeeName).price();
    }
}
