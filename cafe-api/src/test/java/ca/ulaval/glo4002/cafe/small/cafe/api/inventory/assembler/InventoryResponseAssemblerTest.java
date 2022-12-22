package ca.ulaval.glo4002.cafe.small.cafe.api.inventory.assembler;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.inventory.assembler.InventoryResponseAssembler;
import ca.ulaval.glo4002.cafe.api.inventory.response.InventoryResponse;
import ca.ulaval.glo4002.cafe.application.payload.InventoryPayload;
import ca.ulaval.glo4002.cafe.domain.inventory.Ingredient;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Inventory;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryResponseAssemblerTest {
    private InventoryResponseAssembler inventoryResponseAssembler;

    @BeforeEach
    public void createAssembler() {
        inventoryResponseAssembler = new InventoryResponseAssembler();
    }

    @Test
    public void givenEmptyInventory_whenAssemblingInventoryResponse_shouldReturnCorrectResponse() {
        InventoryPayload inventoryPayload = new InventoryPayload(new HashMap<>());

        InventoryResponse actualResponse = inventoryResponseAssembler.toInventoryResponse(inventoryPayload);

        InventoryResponse expectedResponse = new InventoryResponse(0, 0, 0, 0);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void givenInventory_whenAssemblingInventoryResponse_shouldReturnCorrectResponse() {
        Inventory inventory = new Inventory();
        inventory.add(List.of(new Ingredient(IngredientType.Chocolate, new Quantity(1))));
        InventoryPayload inventoryPayload = InventoryPayload.fromInventory(inventory);

        InventoryResponse actualResponse = inventoryResponseAssembler.toInventoryResponse(inventoryPayload);

        InventoryResponse expectedResponse = new InventoryResponse(1, 0, 0, 0);
        assertEquals(expectedResponse, actualResponse);
    }

}
