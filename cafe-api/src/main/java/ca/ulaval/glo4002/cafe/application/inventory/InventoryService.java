package ca.ulaval.glo4002.cafe.application.inventory;

import ca.ulaval.glo4002.cafe.application.inventory.payload.InventoryPayload;
import ca.ulaval.glo4002.cafe.application.inventory.query.IngredientsQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;

public class InventoryService {
    private final CafeRepository cafeRepository;

    public InventoryService(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    public InventoryPayload getInventory() {
        Cafe cafe = cafeRepository.get();
        return InventoryPayload.fromInventory(cafe.getInventory());
    }

    public void addIngredientsToInventory(IngredientsQuery ingredientsQuery) {
        Cafe cafe = cafeRepository.get();
        cafe.addIngredientsToInventory(ingredientsQuery.ingredients());
        cafeRepository.saveOrUpdate(cafe);
    }
}
