package ca.ulaval.glo4002.cafe.small.cafe.application.inventory;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.inventory.InventoryService;
import ca.ulaval.glo4002.cafe.application.inventory.payload.InventoryPayload;
import ca.ulaval.glo4002.cafe.application.inventory.query.IngredientsQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.fixture.CafeFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InventoryServiceTest {
    private static final Cafe A_CAFE = new CafeFixture().build();
    private static final IngredientsQuery AN_INGREDIENTS_QUERY = new IngredientsQuery(1, 2, 3, 4);

    private InventoryService inventoryService;
    private CafeRepository cafeRepository;

    @BeforeEach
    public void createInventoryService() {
        cafeRepository = mock(CafeRepository.class);
        inventoryService = new InventoryService(cafeRepository);
    }

    @Test
    public void whenAddingIngredientsToInventory_shouldGetCafe() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        inventoryService.addIngredientsToInventory(AN_INGREDIENTS_QUERY);

        verify(cafeRepository).get();
    }

    @Test
    public void whenAddingIngredientsToInventory_shouldAddIngredientsInInventory() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        inventoryService.addIngredientsToInventory(AN_INGREDIENTS_QUERY);

        verify(mockCafe).addIngredientsToInventory(AN_INGREDIENTS_QUERY.ingredients());
    }

    @Test
    public void whenAddingIngredientsToInventory_shouldUpdateCafe() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        inventoryService.addIngredientsToInventory(AN_INGREDIENTS_QUERY);

        verify(cafeRepository).saveOrUpdate(mockCafe);
    }

    @Test
    public void whenGettingInventory_shouldGetCafe() {
        when(cafeRepository.get()).thenReturn(A_CAFE);

        inventoryService.getInventory();

        verify(cafeRepository).get();
    }

    @Test
    public void whenGettingInventory_shouldReturnInventoryPayload() {
        when(cafeRepository.get()).thenReturn(A_CAFE);
        InventoryPayload expectedInventoryPayload = new InventoryPayload(new HashMap<>());

        InventoryPayload actualInventoryPayload = inventoryService.getInventory();

        assertEquals(expectedInventoryPayload, actualInventoryPayload);
    }
}
