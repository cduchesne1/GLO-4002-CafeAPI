package ca.ulaval.glo4002.cafe.application.inventory.payload;

import java.util.Map;

import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Inventory;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;

public record InventoryPayload(Map<IngredientType, Quantity> ingredients) {
    public static InventoryPayload fromInventory(Inventory inventory) {
        return new InventoryPayload(inventory.getIngredients());
    }
}
