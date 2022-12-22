package ca.ulaval.glo4002.cafe.api.inventory.assembler;

import ca.ulaval.glo4002.cafe.api.inventory.response.InventoryResponse;
import ca.ulaval.glo4002.cafe.application.payload.InventoryPayload;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;

public class InventoryResponseAssembler {
    public InventoryResponse toInventoryResponse(InventoryPayload inventoryPayload) {
        return new InventoryResponse(inventoryPayload.ingredients().containsKey(IngredientType.Chocolate) ?
            inventoryPayload.ingredients().get(IngredientType.Chocolate).quantity().value() : 0,
            inventoryPayload.ingredients().containsKey(IngredientType.Espresso) ?
                inventoryPayload.ingredients().get(IngredientType.Espresso).quantity().value() : 0,
            inventoryPayload.ingredients().containsKey(IngredientType.Milk) ? inventoryPayload.ingredients().get(IngredientType.Milk).quantity().value() : 0,
            inventoryPayload.ingredients().containsKey(IngredientType.Water) ? inventoryPayload.ingredients().get(IngredientType.Water).quantity().value() : 0);
    }
}
