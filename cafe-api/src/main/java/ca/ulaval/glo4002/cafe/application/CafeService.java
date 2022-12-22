package ca.ulaval.glo4002.cafe.application;

import java.util.List;

import ca.ulaval.glo4002.cafe.application.payload.InventoryPayload;
import ca.ulaval.glo4002.cafe.application.payload.LayoutPayload;
import ca.ulaval.glo4002.cafe.application.query.IngredientsQuery;
import ca.ulaval.glo4002.cafe.application.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeFactory;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;

public class CafeService {
    private final CafeRepository cafeRepository;
    private final CafeFactory cafeFactory;

    public CafeService(CafeRepository cafeRepository, CafeFactory cafeFactory) {
        this.cafeRepository = cafeRepository;
        this.cafeFactory = cafeFactory;
    }

    public void initializeCafe() {
        Cafe cafe = cafeFactory.createCafe();
        cafeRepository.saveOrUpdate(cafe);
    }

    public LayoutPayload getLayout() {
        Cafe cafe = cafeRepository.get();
        return LayoutPayload.fromCafe(cafe);
    }

    public void updateConfiguration(UpdateConfigurationQuery updateConfigurationQuery) {
        Cafe cafe = cafeRepository.get();
        CafeConfiguration cafeConfiguration =
            new CafeConfiguration(updateConfigurationQuery.cubeSize(), updateConfigurationQuery.cafeName(), updateConfigurationQuery.reservationType(),
                updateConfigurationQuery.location(), updateConfigurationQuery.groupTipRate());
        cafe.updateConfiguration(cafeConfiguration);
        cafe.close();
        cafeRepository.saveOrUpdate(cafe);
    }

    public void closeCafe() {
        Cafe cafe = cafeRepository.get();
        cafe.close();
        cafeRepository.saveOrUpdate(cafe);
    }

    public void addIngredientsToInventory(IngredientsQuery ingredientsQuery) {
        Cafe cafe = cafeRepository.get();
        cafe.addIngredientsToInventory(List.of(ingredientsQuery.chocolate(), ingredientsQuery.milk(), ingredientsQuery.water(), ingredientsQuery.espresso()));
        cafeRepository.saveOrUpdate(cafe);
    }

    public InventoryPayload getInventory() {
        Cafe cafe = cafeRepository.get();
        return InventoryPayload.fromInventory(cafe.getInventory());
    }
}
