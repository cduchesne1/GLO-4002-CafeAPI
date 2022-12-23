package ca.ulaval.glo4002.cafe.domain.order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;

public record Order(List<Coffee> items) {
    public Order addAllItems(Order otherOrder) {
        return new Order(Stream.concat(items().stream(), otherOrder.items().stream()).toList());
    }

    public Map<IngredientType, Quantity> ingredientsNeeded() {
        return items.stream().flatMap(coffee -> coffee.recipe().ingredients().entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Quantity::add));
    }
}
