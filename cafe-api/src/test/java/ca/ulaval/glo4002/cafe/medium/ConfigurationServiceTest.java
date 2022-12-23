package ca.ulaval.glo4002.cafe.medium;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.configuration.ConfigurationService;
import ca.ulaval.glo4002.cafe.application.configuration.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.application.configuration.query.UpdateMenuQuery;
import ca.ulaval.glo4002.cafe.application.inventory.query.IngredientsQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.Customer;
import ca.ulaval.glo4002.cafe.domain.ordering.Order;
import ca.ulaval.glo4002.cafe.domain.reservation.ReservationStrategyFactory;
import ca.ulaval.glo4002.cafe.fixture.CustomerFixture;
import ca.ulaval.glo4002.cafe.util.CafeRepositoryTestUtil;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationServiceTest {
    private static final CafeName NEW_CAFE_NAME = new CafeName("Les 4-Ogres");
    private static final UpdateConfigurationQuery UPDATE_CONFIGURATION_QUERY =
        new UpdateConfigurationQuery(5, NEW_CAFE_NAME.value(), "Default", "CA", "QC", "", 5);
    private static final UpdateMenuQuery AN_UPDATE_MENU_QUERY =
        new UpdateMenuQuery("Pumpkin Latte", new IngredientsQuery(0, 0, 50, 50), 4);

    private ConfigurationService configurationService;
    private CafeRepository cafeRepository;

    @BeforeEach
    public void setupService() {
        cafeRepository = CafeRepositoryTestUtil.createCafeRepositoryWithDefaultCafe();
        configurationService = new ConfigurationService(cafeRepository, new ReservationStrategyFactory());
    }

    @Test
    public void whenUpdatingConfiguration_shouldUpdateConfiguration() {
        configurationService.updateConfiguration(UPDATE_CONFIGURATION_QUERY);

        Cafe cafe = cafeRepository.get();
        assertEquals(NEW_CAFE_NAME, cafe.getName());
    }

    @Test
    public void whenUpdatingMenu_shouldUpdateMenu() {
        configurationService.updateMenu(AN_UPDATE_MENU_QUERY);

        Cafe cafe = cafeRepository.get();
        Customer customer = new CustomerFixture().build();
        cafe.checkIn(customer, Optional.empty());
        cafe.addIngredientsToInventory(AN_UPDATE_MENU_QUERY.ingredients());
        assertDoesNotThrow(() -> cafe.placeOrder(customer.getId(), new Order(List.of(AN_UPDATE_MENU_QUERY.name()))));
    }
}
