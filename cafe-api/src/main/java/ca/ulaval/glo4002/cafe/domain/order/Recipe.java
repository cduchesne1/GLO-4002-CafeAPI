package ca.ulaval.glo4002.cafe.domain.order;

import java.util.Map;

import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;

public record Recipe(Map<IngredientType, Quantity> ingredients) {
}
