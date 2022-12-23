package ca.ulaval.glo4002.cafe.domain.ordering.menu;

import java.util.Map;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;
import ca.ulaval.glo4002.cafe.domain.ordering.CoffeeName;

public record Coffee(CoffeeName name, Amount price, Map<IngredientType, Quantity> ingredients) {
}
