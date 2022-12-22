package ca.ulaval.glo4002.cafe.medium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.inventory.InventoryService;
import ca.ulaval.glo4002.cafe.application.inventory.payload.InventoryPayload;
import ca.ulaval.glo4002.cafe.application.inventory.query.IngredientsQuery;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.util.CafeRepositoryTestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryServiceTest {
    private static final IngredientsQuery INGREDIENTS_QUERY = new IngredientsQuery(25, 20, 15, 10);

    private InventoryService inventoryService;
    private CafeRepository cafeRepository;

    @BeforeEach
    public void setupService() {
        cafeRepository = CafeRepositoryTestUtil.createCafeRepositoryWithDefaultCafe();
        inventoryService = new InventoryService(cafeRepository);
    }

    @Test
    public void whenAddingIngredientsToInventory_shouldAddIngredientsToInventory() {
        inventoryService.addIngredientsToInventory(INGREDIENTS_QUERY);

        InventoryPayload inventory = inventoryService.getInventory();
        assertEquals(INGREDIENTS_QUERY.chocolate().quantity(), inventory.ingredients().get(IngredientType.Chocolate).quantity());
        assertEquals(INGREDIENTS_QUERY.milk().quantity(), inventory.ingredients().get(IngredientType.Milk).quantity());
        assertEquals(INGREDIENTS_QUERY.water().quantity(), inventory.ingredients().get(IngredientType.Water).quantity());
        assertEquals(INGREDIENTS_QUERY.espresso().quantity(), inventory.ingredients().get(IngredientType.Espresso).quantity());
    }
}
