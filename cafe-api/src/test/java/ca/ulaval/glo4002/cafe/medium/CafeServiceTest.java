package ca.ulaval.glo4002.cafe.medium;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.CafeService;
import ca.ulaval.glo4002.cafe.application.inventory.InventoryService;
import ca.ulaval.glo4002.cafe.application.inventory.query.IngredientsQuery;
import ca.ulaval.glo4002.cafe.application.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerNoBillException;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.Customer;
import ca.ulaval.glo4002.cafe.domain.reservation.Reservation;
import ca.ulaval.glo4002.cafe.fixture.CustomerFixture;
import ca.ulaval.glo4002.cafe.fixture.ReservationFixture;
import ca.ulaval.glo4002.cafe.util.CafeRepositoryTestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CafeServiceTest {
    private static final CafeName NEW_CAFE_NAME = new CafeName("Les 4-Ogres");
    private static final Customer A_CUSTOMER = new CustomerFixture().build();
    private static final Reservation A_RESERVATION = new ReservationFixture().build();
    private static final IngredientsQuery INGREDIENTS_QUERY = new IngredientsQuery(100, 100, 100, 100);
    private static final UpdateConfigurationQuery UPDATE_CONFIGURATION_QUERY =
        new UpdateConfigurationQuery(5, NEW_CAFE_NAME.value(), "Default", "CA", "QC", "", 5);

    private CafeService cafeService;
    private InventoryService inventoryService;
    private Cafe cafe;
    private CafeRepository cafeRepository;

    @BeforeEach
    public void instanciateAttributes() {
        cafeRepository = CafeRepositoryTestUtil.createCafeRepositoryWithDefaultCafe();
        cafeService = new CafeService(cafeRepository);
        inventoryService = new InventoryService(cafeRepository);
        cafe = cafeRepository.get();
    }

    @Test
    public void whenUpdatingConfiguration_shouldUpdateConfiguration() {
        cafeService.updateConfiguration(UPDATE_CONFIGURATION_QUERY);

        assertEquals(NEW_CAFE_NAME, cafeService.getLayout().name());
    }

    @Test
    public void givenAReservation_whenClosing_shouldClearReservations() {
        cafe.makeReservation(A_RESERVATION);

        cafeService.closeCafe();

        cafe = cafeRepository.get();
        assertEquals(0, cafe.getReservations().size());
    }

    @Test
    public void givenASavedBill_whenClosing_shouldClearBills() {
        cafe.checkIn(A_CUSTOMER, Optional.empty());
        cafe.checkOut(A_CUSTOMER.getId());

        cafeService.closeCafe();
        cafe.checkIn(A_CUSTOMER, Optional.empty());

        cafe = cafeRepository.get();
        assertThrows(CustomerNoBillException.class, () -> cafe.getCustomerBill(A_CUSTOMER.getId()));
    }

    @Test
    public void givenNonEmptyInventory_whenClosing_shouldClearInventory() {
        inventoryService.addIngredientsToInventory(INGREDIENTS_QUERY);

        cafeService.closeCafe();

        assertEquals(0, inventoryService.getInventory().ingredients().size());
    }
}
