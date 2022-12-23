package ca.ulaval.glo4002.cafe.domain.ordering.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.exception.InvalidMenuOrderException;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;
import ca.ulaval.glo4002.cafe.domain.ordering.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.ordering.Order;

public class Menu {
    private final Map<CoffeeName, Coffee> coffees;
    private final Map<CoffeeName, Coffee> initialCoffees;

    public Menu(Map<CoffeeName, Coffee> coffees) {
        this.coffees = new HashMap<>(coffees);
        this.initialCoffees = new HashMap<>(coffees);
    }

    public void validateItemsAreInMenu(Order order) {
        if (order.items().stream().anyMatch(item -> !coffees.containsKey(item))) {
            throw new InvalidMenuOrderException();
        }
    }

    public Map<IngredientType, Quantity> getIngredientsNeeded(Order order) {
        return order.items().stream().map(coffees::get).flatMap(coffee -> coffee.ingredients().entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Quantity::add));
    }

    public Amount getItemPrice(CoffeeName coffeeName) {
        return coffees.get(coffeeName).price();
    }

    public void addItemToMenu(CoffeeName name, Map<IngredientType, Quantity> ingredients, Amount cost) {
        coffees.put(name, new Coffee(name, cost, ingredients));
    }

    public void removeCustomCoffees() {
        coffees.keySet().retainAll(initialCoffees.keySet());
    }
}
