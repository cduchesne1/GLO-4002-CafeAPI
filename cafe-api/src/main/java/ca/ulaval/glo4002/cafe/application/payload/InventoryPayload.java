package ca.ulaval.glo4002.cafe.application.payload;

import java.util.HashMap;

import ca.ulaval.glo4002.cafe.domain.inventory.Ingredient;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Inventory;

public record InventoryPayload(HashMap<IngredientType, Ingredient> ingredients) {
    public static InventoryPayload fromInventory(Inventory inventory) {
        return new InventoryPayload(inventory.getIngredients());
    }
}
