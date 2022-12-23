package ca.ulaval.glo4002.cafe.small.cafe.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.PointOfSale;
import ca.ulaval.glo4002.cafe.domain.bill.Bill;
import ca.ulaval.glo4002.cafe.domain.bill.BillFactory;
import ca.ulaval.glo4002.cafe.domain.bill.TipRate;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerAlreadyVisitedException;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerNoBillException;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerNotFoundException;
import ca.ulaval.glo4002.cafe.domain.exception.DuplicateGroupNameException;
import ca.ulaval.glo4002.cafe.domain.exception.InsufficientIngredientsException;
import ca.ulaval.glo4002.cafe.domain.exception.InsufficientSeatsException;
import ca.ulaval.glo4002.cafe.domain.exception.NoGroupSeatsException;
import ca.ulaval.glo4002.cafe.domain.exception.NoReservationsFoundException;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Inventory;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;
import ca.ulaval.glo4002.cafe.domain.layout.Layout;
import ca.ulaval.glo4002.cafe.domain.layout.cube.Cube;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.Seat;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.SeatNumber;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.Customer;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerId;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Province;
import ca.ulaval.glo4002.cafe.domain.location.State;
import ca.ulaval.glo4002.cafe.domain.order.Coffee;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeType;
import ca.ulaval.glo4002.cafe.domain.order.Order;
import ca.ulaval.glo4002.cafe.domain.reservation.BookingRegister;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupName;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupSize;
import ca.ulaval.glo4002.cafe.domain.reservation.Reservation;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.DefaultStrategy;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.FullCubesStrategy;
import ca.ulaval.glo4002.cafe.fixture.CafeConfigurationFixture;
import ca.ulaval.glo4002.cafe.fixture.CustomerFixture;
import ca.ulaval.glo4002.cafe.fixture.OrderFixture;
import ca.ulaval.glo4002.cafe.fixture.ReservationFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CafeTest {
    private static final List<CubeName> SOME_CUBE_NAMES = List.of(new CubeName("Bob"), new CubeName("John"));
    private static final List<CubeName> SOME_UNORDERED_CUBE_NAMES = List.of(new CubeName("B"), new CubeName("A"));
    private static final List<CubeName> A_CUBE_NAME = List.of(new CubeName("aName"));
    private static final List<CubeName> TWO_CUBE_NAMES = List.of(new CubeName("Bob"), new CubeName("John"));
    private static final int TWO_SEATS_PER_CUBE = 2;
    private static final Customer A_CUSTOMER = new CustomerFixture().withCustomerId(new CustomerId("125")).build();
    private static final Customer ANOTHER_CUSTOMER = new CustomerFixture().withCustomerId(new CustomerId("121135")).build();
    private static final Reservation A_RESERVATION_FOR_TWO = new ReservationFixture().withGroupSize(new GroupSize(2)).build();
    private static final Reservation ANOTHER_RESERVATION_FOR_TWO =
        new ReservationFixture().withGroupName(new GroupName("Another")).withGroupSize(new GroupSize(2)).build();
    private static final Order AN_ORDER = new OrderFixture().build();
    private static final Order ANOTHER_ORDER = new OrderFixture().withItems(List.of(new Coffee(CoffeeType.Espresso))).build();
    private static final CafeConfiguration A_DEFAULT_CONFIGURATION = new CafeConfigurationFixture().build();

    private Cafe cafe;

    @BeforeEach
    public void createCafe() {
        Layout layout = new Layout(A_DEFAULT_CONFIGURATION.cubeSize(), SOME_CUBE_NAMES);
        cafe = new Cafe(A_DEFAULT_CONFIGURATION, layout, new BookingRegister(), new PointOfSale(new BillFactory()), new Inventory());
    }

    @Test
    public void whenCreatingCafe_shouldHaveNoReservations() {
        assertTrue(cafe.getReservations().isEmpty());
    }

    @Test
    public void whenGettingLayout_shouldHaveProvidedNumberOfCubesInLayout() {
        Layout layout = cafe.getLayout();

        assertEquals(SOME_CUBE_NAMES.size(), layout.getCubes().size());
    }

    @Test
    public void whenGettingLayout_shouldHaveCubesWithProvidedNamesInLayout() {
        Layout layout = cafe.getLayout();

        assertEquals(SOME_CUBE_NAMES.stream().sorted().toList(), layout.getCubes().stream().map(Cube::getName).sorted().toList());
    }

    @Test
    public void whenGettingLayout_shouldHaveCubesInAlphabeticalOrderOfNameInLayout() {
        cafe = new Cafe(A_DEFAULT_CONFIGURATION, new Layout(A_DEFAULT_CONFIGURATION.cubeSize(), SOME_UNORDERED_CUBE_NAMES), new BookingRegister(),
            new PointOfSale(new BillFactory()), new Inventory());
        Layout layout = cafe.getLayout();

        assertEquals(SOME_UNORDERED_CUBE_NAMES.stream().sorted().toList(), layout.getCubes().stream().map(Cube::getName).toList());
    }

    @Test
    public void whenGettingLayout_shouldHaveCubesWithProvidedNumberOfSeatsInLayout() {
        int providedCubeSize = 2;
        cafe = new Cafe(new CafeConfigurationFixture().withCubeSize(providedCubeSize).build(), new Layout(providedCubeSize, SOME_CUBE_NAMES),
            new BookingRegister(), new PointOfSale(new BillFactory()), new Inventory());

        Layout layout = cafe.getLayout();

        assertEquals(providedCubeSize, layout.getCubes().get(0).getNumberOfSeats());
    }

    @Test
    public void whenGettingLayout_shouldHaveCubesWithIncrementingSeatNumbersInLayout() {
        cafe = new Cafe(new CafeConfigurationFixture().withCubeSize(TWO_SEATS_PER_CUBE).build(), new Layout(TWO_SEATS_PER_CUBE, TWO_CUBE_NAMES),
            new BookingRegister(), new PointOfSale(new BillFactory()), new Inventory());

        Layout layout = cafe.getLayout();
        List<Integer> actualSeatNumbers = layout.getCubes().stream().map(Cube::getSeats).flatMap(List::stream).map(seat -> seat.getNumber().value()).toList();

        List<Integer> expectedSeatNumbers = List.of(1, 2, 3, 4);
        assertEquals(expectedSeatNumbers, actualSeatNumbers);
    }

    @Test
    public void givenNoReservationNorCustomer_whenGettingLayout_shouldHaveAllSeatsAvailableInLayout() {
        cafe = new Cafe(new CafeConfigurationFixture().withCubeSize(TWO_SEATS_PER_CUBE).build(), new Layout(TWO_SEATS_PER_CUBE, TWO_CUBE_NAMES),
            new BookingRegister(), new PointOfSale(new BillFactory()), new Inventory());

        Layout layout = cafe.getLayout();
        int numberOfAvailableSeats = (int) layout.getCubes().stream().map(Cube::getSeats).flatMap(List::stream).filter(Seat::isCurrentlyAvailable).count();

        assertEquals(SOME_CUBE_NAMES.size() * TWO_SEATS_PER_CUBE, numberOfAvailableSeats);
    }

    @Test
    public void givenReservation_whenGettingLayout_shouldHaveMatchingSeatsReservedInLayout() {
        cafe = new Cafe(new CafeConfigurationFixture().withCubeSize(TWO_SEATS_PER_CUBE).build(), new Layout(TWO_SEATS_PER_CUBE, TWO_CUBE_NAMES),
            new BookingRegister(), new PointOfSale(new BillFactory()), new Inventory());
        cafe.makeReservation(A_RESERVATION_FOR_TWO);

        Layout layout = cafe.getLayout();
        int numberOfReservedSeats = (int) layout.getCubes().stream().map(Cube::getSeats).flatMap(List::stream).filter(Seat::isCurrentlyReserved).count();

        assertEquals(2, numberOfReservedSeats);
    }

    @Test
    public void givenNoAvailableSeats_whenCheckingIn_shouldThrowInsufficientSeatsException() {
        cafe = new Cafe(new CafeConfigurationFixture().withCubeSize(3).build(), new Layout(3, A_CUBE_NAME), new BookingRegister(),
            new PointOfSale(new BillFactory()), new Inventory());
        cafe.checkIn(ANOTHER_CUSTOMER, Optional.empty());
        cafe.makeReservation(A_RESERVATION_FOR_TWO);
        Customer aCustomer = new CustomerFixture().build();

        assertThrows(InsufficientSeatsException.class, () -> cafe.checkIn(aCustomer, Optional.empty()));
    }

    @Test
    public void givenNewCustomerWithoutReservation_whenCheckingIn_shouldOccupyFirstAvailableSeat() {
        cafe.checkIn(ANOTHER_CUSTOMER, Optional.empty());
        cafe.makeReservation(A_RESERVATION_FOR_TWO);
        Customer aCustomer = new CustomerFixture().build();

        cafe.checkIn(aCustomer, Optional.empty());

        assertEquals(new SeatNumber(4), cafe.getSeatByCustomerId(aCustomer.getId()).getNumber());
    }

    @Test
    public void givenNewCustomerWithReservation_whenCheckingIn_shouldOccupyFirstReservedSeat() {
        cafe = new Cafe(new CafeConfigurationFixture().withCubeSize(3).build(), new Layout(3, A_CUBE_NAME), new BookingRegister(),
            new PointOfSale(new BillFactory()), new Inventory());
        cafe.checkIn(ANOTHER_CUSTOMER, Optional.empty());
        cafe.makeReservation(A_RESERVATION_FOR_TWO);
        Customer aCustomer = new CustomerFixture().build();

        cafe.checkIn(aCustomer, Optional.of(A_RESERVATION_FOR_TWO.name()));

        assertEquals(new SeatNumber(2), cafe.getSeatByCustomerId(aCustomer.getId()).getNumber());
    }

    @Test
    public void givenNewCustomerWithReservationButNoMoreReservedSeats_whenCheckingIn_shouldThrowNoGroupSeatsException() {
        cafe = new Cafe(new CafeConfigurationFixture().withCubeSize(3).build(), new Layout(3, A_CUBE_NAME), new BookingRegister(),
            new PointOfSale(new BillFactory()), new Inventory());
        cafe.makeReservation(A_RESERVATION_FOR_TWO);
        cafe.checkIn(A_CUSTOMER, Optional.of(A_RESERVATION_FOR_TWO.name()));
        cafe.checkIn(ANOTHER_CUSTOMER, Optional.of(A_RESERVATION_FOR_TWO.name()));

        assertThrows(NoGroupSeatsException.class, () -> cafe.checkIn(new CustomerFixture().build(), Optional.of(A_RESERVATION_FOR_TWO.name())));
    }

    @Test
    public void givenNewCustomerWithInvalidReservation_whenCheckingIn_shouldThrowNoReservationFoundException() {
        cafe = new Cafe(new CafeConfigurationFixture().withCubeSize(3).build(), new Layout(3, A_CUBE_NAME), new BookingRegister(),
            new PointOfSale(new BillFactory()), new Inventory());
        cafe.makeReservation(A_RESERVATION_FOR_TWO);
        Customer aCustomer = new CustomerFixture().build();

        assertThrows(NoReservationsFoundException.class, () -> cafe.checkIn(aCustomer, Optional.of(new GroupName("invalid"))));
    }

    @Test
    public void givenCustomerWhoAlreadyVisitedToday_whenCheckingIn_shouldThrowCustomerAlreadyVisitedException() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());

        assertThrows(CustomerAlreadyVisitedException.class, () -> cafe.checkIn(aCustomer, Optional.empty()));
    }

    @Test
    public void givenCheckedInCustomer_whenGettingSeatByCustomerId_shouldReturnSeatWithCustomer() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());

        Seat actualSeat = cafe.getSeatByCustomerId(aCustomer.getId());

        assertEquals(aCustomer, actualSeat.getCustomer().get());
        assertEquals(cafe.getLayout().getCubes().get(0).getSeats().get(0), actualSeat);
    }

    @Test
    public void givenNotCheckedInCustomer_whenGettingSeatByCustomerId_shouldThrowCustomerNotFoundException() {
        Customer aCustomer = new CustomerFixture().build();

        assertThrows(CustomerNotFoundException.class, () -> cafe.getSeatByCustomerId(aCustomer.getId()));
    }

    @Test
    public void givenClosedCafe_whenGettingSeatByCustomerId_shouldThrowCustomerNotFoundException() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.close();

        assertThrows(CustomerNotFoundException.class, () -> cafe.getSeatByCustomerId(aCustomer.getId()));
    }

    @Test
    public void whenMakingReservation_shouldSaveReservation() {
        cafe.makeReservation(A_RESERVATION_FOR_TWO);

        assertTrue(cafe.getReservations().contains(A_RESERVATION_FOR_TWO));
    }

    @Test
    public void whenMakingReservation_shouldUseProvidedReservationStrategy() {
        Layout layout = new Layout(A_DEFAULT_CONFIGURATION.cubeSize(), SOME_CUBE_NAMES);
        cafe = new Cafe(new CafeConfigurationFixture().withReservationStrategy(new FullCubesStrategy()).build(), layout, new BookingRegister(),
            new PointOfSale(new BillFactory()), new Inventory());

        cafe.makeReservation(A_RESERVATION_FOR_TWO);

        assertTrue(cafe.getLayout().getCubes().get(0).getSeats().stream().allMatch(Seat::isCurrentlyReserved));
    }

    @Test
    public void givenAlreadyUsedGroupName_whenMakingReservation_shouldThrowDuplicateGroupNameException() {
        cafe.makeReservation(A_RESERVATION_FOR_TWO);

        assertThrows(DuplicateGroupNameException.class, () -> cafe.makeReservation(A_RESERVATION_FOR_TWO));
    }

    @Test
    public void givenNotEnoughSeats_whenMakingReservation_shouldThrowInsufficientSeatsException() {
        cafe = new Cafe(new CafeConfigurationFixture().withCubeSize(TWO_SEATS_PER_CUBE).build(), new Layout(TWO_SEATS_PER_CUBE, A_CUBE_NAME),
            new BookingRegister(), new PointOfSale(new BillFactory()), new Inventory());
        cafe.makeReservation(A_RESERVATION_FOR_TWO);

        assertThrows(InsufficientSeatsException.class, () -> cafe.makeReservation(ANOTHER_RESERVATION_FOR_TWO));
    }

    @Test
    public void givenNewReservationStrategy_whenUpdatingConfiguration_shouldUseNewReservationStrategy() {
        Layout layout = new Layout(A_DEFAULT_CONFIGURATION.cubeSize(), SOME_CUBE_NAMES);
        cafe = new Cafe(new CafeConfigurationFixture().withReservationStrategy(new DefaultStrategy()).build(), layout, new BookingRegister(),
            new PointOfSale(new BillFactory()), new Inventory());

        cafe.updateConfiguration(new CafeConfigurationFixture().withReservationStrategy(new FullCubesStrategy()).build());

        cafe.makeReservation(A_RESERVATION_FOR_TWO);
        assertTrue(cafe.getLayout().getCubes().get(0).getSeats().stream().allMatch(Seat::isCurrentlyReserved));
    }

    @Test
    public void givenNewName_whenUpdatingConfiguration_shouldUpdateName() {
        CafeName newName = new CafeName("newName");

        cafe.updateConfiguration(new CafeConfigurationFixture().withName(newName).build());

        assertEquals(newName, cafe.getName());
    }

    @Test
    public void givenNewCubeSize_whenUpdatingConfiguration_shouldUpdateCubeSize() {
        Layout layout = new Layout(A_DEFAULT_CONFIGURATION.cubeSize(), SOME_CUBE_NAMES);
        cafe =
            new Cafe(new CafeConfigurationFixture().withCubeSize(TWO_SEATS_PER_CUBE).build(), layout, new BookingRegister(), new PointOfSale(new BillFactory()),
                new Inventory());
        int newCubeSize = 3;

        cafe.updateConfiguration(new CafeConfigurationFixture().withCubeSize(newCubeSize).build());
        cafe.close();

        assertEquals(newCubeSize, cafe.getLayout().getCubes().get(0).getNumberOfSeats());
    }

    @Test
    public void givenNewLocation_whenUpdatingConfiguration_shouldUpdateLocation() {
        Layout layout = new Layout(A_DEFAULT_CONFIGURATION.cubeSize(), SOME_CUBE_NAMES);
        cafe = new Cafe(new CafeConfigurationFixture().withCountry(Country.CL).build(), layout, new BookingRegister(), new PointOfSale(new BillFactory()),
            new Inventory());
        cafe.addIngredientsToInventory(AN_ORDER.ingredientsNeeded());

        cafe.updateConfiguration(new CafeConfigurationFixture().withCountry(Country.None).build());
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.placeOrder(aCustomer.getId(), AN_ORDER);
        cafe.checkOut(aCustomer.getId());

        assertEquals(new Amount(0), cafe.getCustomerBill(aCustomer.getId()).taxes());
    }

    @Test
    public void givenNewGroupTipRate_whenUpdatingConfiguration_shouldUpdateGroupTipRate() {
        Layout layout = new Layout(A_DEFAULT_CONFIGURATION.cubeSize(), SOME_CUBE_NAMES);
        cafe = new Cafe(new CafeConfigurationFixture().withGroupTipRate(new TipRate(0.20f)).build(), layout, new BookingRegister(),
            new PointOfSale(new BillFactory()), new Inventory());
        cafe.addIngredientsToInventory(AN_ORDER.ingredientsNeeded());

        cafe.updateConfiguration(new CafeConfigurationFixture().withGroupTipRate(new TipRate(0.15f)).build());
        Customer aCustomer = new CustomerFixture().build();
        cafe.makeReservation(A_RESERVATION_FOR_TWO);
        cafe.checkIn(aCustomer, Optional.of(A_RESERVATION_FOR_TWO.name()));
        cafe.placeOrder(aCustomer.getId(), AN_ORDER);
        cafe.checkOut(aCustomer.getId());

        assertNotEquals(new Amount(0), cafe.getCustomerBill(aCustomer.getId()).tip());
    }

    @Test
    public void whenPlacingOrder_shouldAssignItToCustomer() {
        Cafe cafe = cafeWithEnoughInventory();
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());

        cafe.placeOrder(aCustomer.getId(), AN_ORDER);

        assertEquals(AN_ORDER, cafe.getOrderByCustomerId(aCustomer.getId()));
    }

    @Test
    public void givenCustomerWithPreviousOrder_whenPlacingOrder_shouldAppendNewOrderToCustomer() {
        Cafe cafe = cafeWithEnoughInventory();
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.placeOrder(aCustomer.getId(), AN_ORDER);

        cafe.placeOrder(aCustomer.getId(), ANOTHER_ORDER);

        assertEquals(AN_ORDER.addAllItems(ANOTHER_ORDER), cafe.getOrderByCustomerId(aCustomer.getId()));
    }

    @Test
    public void givenNotCheckedInCustomer_whenPlacingOrder_shouldThrowCustomerNotFoundException() {
        Cafe cafe = cafeWithEnoughInventory();

        assertThrows(CustomerNotFoundException.class, () -> cafe.placeOrder(new CustomerId("Invalid"), AN_ORDER));
    }

    @Test
    public void givenEnoughIngredients_whenPlacingOrder_shouldConsumeIngredientsFromInventory() {
        cafe.addIngredientsToInventory(AN_ORDER.ingredientsNeeded());
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());

        cafe.placeOrder(aCustomer.getId(), AN_ORDER);

        assertTrue(cafe.getInventory().getIngredients().isEmpty());
    }

    @Test
    public void givenNotEnoughIngredients_whenPlacingOrder_shouldThrowInsufficientIngredientsException() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());

        assertThrows(InsufficientIngredientsException.class, () -> cafe.placeOrder(aCustomer.getId(), AN_ORDER));
    }

    @Test
    public void givenNotEnoughIngredients_whenPlacingOrder_shouldNotConsumeAnyIngredient() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.addIngredientsToInventory(AN_ORDER.ingredientsNeeded());

        try {
            cafe.placeOrder(aCustomer.getId(), AN_ORDER.addAllItems(AN_ORDER));
        } catch (InsufficientIngredientsException ignored) {
        }

        assertEquals(cafe.getInventory().getIngredients(), AN_ORDER.ingredientsNeeded());
    }

    @Test
    public void givenNotEnoughIngredients_whenPlacingOrder_shouldNotBeAddedToCustomersOrders() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.addIngredientsToInventory(AN_ORDER.ingredientsNeeded());

        try {
            cafe.placeOrder(aCustomer.getId(), AN_ORDER.addAllItems(AN_ORDER));
        } catch (InsufficientIngredientsException ignored) {
        }

        assertTrue(cafe.getOrderByCustomerId(aCustomer.getId()).items().isEmpty());
    }

    @Test
    public void givenInvalidCustomerId_whenGettingOrderByCustomerId_shouldThrowCustomerNotFoundException() {
        assertThrows(CustomerNotFoundException.class, () -> cafe.getOrderByCustomerId(new CustomerId("Invalid")));
    }

    @Test
    public void givenCheckedInCustomer_whenCheckingOut_shouldFreeSeat() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());

        cafe.checkOut(aCustomer.getId());

        assertTrue(cafe.getLayout().getCubes().get(0).getSeats().get(0).isCurrentlyAvailable());
    }

    @Test
    public void givenLastGroupMemberToLeave_whenCheckingOut_shouldFreeSeatsStillReservedForGroup() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.makeReservation(A_RESERVATION_FOR_TWO);
        cafe.checkIn(aCustomer, Optional.of(A_RESERVATION_FOR_TWO.name()));

        cafe.checkOut(aCustomer.getId());

        assertTrue(cafe.getLayout().getCubes().get(0).getSeats().get(0).isCurrentlyAvailable());
        assertTrue(cafe.getLayout().getCubes().get(0).getSeats().get(1).isCurrentlyAvailable());
    }

    @Test
    public void givenNonexistentCustomer_whenCheckingOut_shouldThrowCustomerNotFoundException() {

        assertThrows(CustomerNotFoundException.class, () -> cafe.checkOut(new CustomerId("Invalid")));
    }

    @Test
    public void givenNoOrdersMade_whenCheckingOut_shouldStillCheckOut() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());

        cafe.checkOut(aCustomer.getId());

        assertEquals(new Amount(0), cafe.getCustomerBill(aCustomer.getId()).total());
        assertThrows(CustomerNotFoundException.class, () -> cafe.getSeatByCustomerId(aCustomer.getId()));
    }

    @Test
    public void givenNonexistentCustomer_whenGettingBill_shouldThrowCustomerNotFoundException() {

        assertThrows(CustomerNotFoundException.class, () -> cafe.getCustomerBill(new CustomerId("Invalid")));
    }

    @Test
    public void givenNotCheckedOutCustomer_whenGettingBill_shouldThrowCustomerNoBillException() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());

        assertThrows(CustomerNoBillException.class, () -> cafe.getCustomerBill(aCustomer.getId()));
    }

    @Test
    public void givenNoOrdersMade_whenGettingBill_shouldGetEmptyBill() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.checkOut(aCustomer.getId());

        Bill bill = cafe.getCustomerBill(aCustomer.getId());

        assertTrue(bill.order().items().isEmpty());
        assertEquals(new Amount(0), bill.subtotal());
        assertEquals(new Amount(0), bill.taxes());
        assertEquals(new Amount(0), bill.total());
    }

    @Test
    public void givenOrdersMade_whenGettingBill_shouldContainMatchingOrders() {
        Cafe cafe = cafeWithEnoughInventory();
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.placeOrder(aCustomer.getId(), AN_ORDER);
        cafe.checkOut(aCustomer.getId());

        Bill bill = cafe.getCustomerBill(aCustomer.getId());

        assertEquals(AN_ORDER.items(), bill.order().items());
    }

    @Test
    public void givenOrdersMade_whenGettingBill_shouldHaveSubtotalMatchingOrdersSum() {
        Cafe cafe = cafeWithEnoughInventory();
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.placeOrder(aCustomer.getId(), AN_ORDER);
        cafe.checkOut(aCustomer.getId());

        Bill bill = cafe.getCustomerBill(aCustomer.getId());

        double expectedSubtotal = AN_ORDER.items().stream().mapToDouble(coffee -> coffee.price().value()).sum();
        assertEquals(expectedSubtotal, bill.subtotal().value());
    }

    @Test
    public void givenOrdersMade_whenGettingBill_shouldHaveTotalAccordingToSubtotalAndTaxes() {
        Cafe cafe = cafeWithEnoughInventory();
        cafe.updateConfiguration(new CafeConfigurationFixture().withCountry(Country.CL).build());
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.placeOrder(aCustomer.getId(), AN_ORDER);
        cafe.checkOut(aCustomer.getId());

        Bill bill = cafe.getCustomerBill(aCustomer.getId());

        float expectedSubtotal = (float) AN_ORDER.items().stream().mapToDouble(coffee -> coffee.price().value()).sum();
        float taxes = expectedSubtotal * 0.19f;
        float expectedTotal = expectedSubtotal + taxes;
        assertEquals(expectedTotal, bill.total().value());
    }

    @Test
    public void givenCountryWithProvince_whenGettingBill_shouldCalculateFederalAndProvincialTaxes() {
        Cafe cafe = cafeWithEnoughInventory();
        cafe.updateConfiguration(new CafeConfigurationFixture().withCountry(Country.CA).withProvince(Province.QC).build());
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.placeOrder(aCustomer.getId(), AN_ORDER);
        cafe.checkOut(aCustomer.getId());

        Bill bill = cafe.getCustomerBill(aCustomer.getId());

        float subtotal = (float) AN_ORDER.items().stream().mapToDouble(coffee -> coffee.price().value()).sum();
        assertEquals(new Amount(subtotal * 0.14975f), bill.taxes());
    }

    @Test
    public void givenCountryWithState_whenGettingBill_shouldCalculateFederalAndStateTaxes() {
        Cafe cafe = cafeWithEnoughInventory();
        cafe.updateConfiguration(new CafeConfigurationFixture().withCountry(Country.US).withState(State.FL).build());
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.placeOrder(aCustomer.getId(), AN_ORDER);
        cafe.checkOut(aCustomer.getId());

        Bill bill = cafe.getCustomerBill(aCustomer.getId());

        float subtotal = (float) AN_ORDER.items().stream().mapToDouble(coffee -> coffee.price().value()).sum();
        assertEquals(new Amount(subtotal * 0.06f), bill.taxes());
    }

    @Test
    public void givenCustomerWithoutGroup_whenGettingBill_shouldNotHaveMandatoryGroupTip() {
        Cafe cafe = cafeWithEnoughInventory();
        cafe.updateConfiguration(new CafeConfigurationFixture().withGroupTipRate(new TipRate(0.15f)).build());
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.placeOrder(aCustomer.getId(), AN_ORDER);
        cafe.checkOut(aCustomer.getId());

        Bill bill = cafe.getCustomerBill(aCustomer.getId());

        assertEquals(new Amount(0), bill.tip());
    }

    @Test
    public void givenCustomerWithGroup_whenGettingBill_shouldHaveMandatoryGroupTip() {
        Cafe cafe = cafeWithEnoughInventory();
        cafe.updateConfiguration(new CafeConfigurationFixture().withGroupTipRate(new TipRate(0.15f)).build());
        Customer aCustomer = new CustomerFixture().build();
        cafe.makeReservation(A_RESERVATION_FOR_TWO);
        cafe.checkIn(aCustomer, Optional.of(A_RESERVATION_FOR_TWO.name()));
        cafe.placeOrder(aCustomer.getId(), AN_ORDER);
        cafe.checkOut(aCustomer.getId());

        Bill bill = cafe.getCustomerBill(aCustomer.getId());

        float subtotal = (float) AN_ORDER.items().stream().mapToDouble(coffee -> coffee.price().value()).sum();
        assertEquals(new Amount(subtotal * 0.15f), bill.tip());
    }

    @Test
    public void givenCountryWithOnlyFederalTax_whenGettingBill_shouldCalculateFederalTax() {
        Cafe cafe = cafeWithEnoughInventory();
        cafe.updateConfiguration(new CafeConfigurationFixture().withCountry(Country.CL).build());
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.placeOrder(aCustomer.getId(), AN_ORDER);
        cafe.checkOut(aCustomer.getId());

        Bill bill = cafe.getCustomerBill(aCustomer.getId());

        float subtotal = (float) AN_ORDER.items().stream().mapToDouble(coffee -> coffee.price().value()).sum();
        assertEquals(new Amount(subtotal * 0.19f), bill.taxes());
    }

    @Test
    public void whenAddingIngredients_shouldBeAddedToInventory() {
        cafe.addIngredientsToInventory(Map.of(IngredientType.Chocolate, new Quantity(10)));

        assertEquals(new Quantity(10), cafe.getInventory().getIngredients().get(IngredientType.Chocolate));
    }

    @Test
    public void whenClosingCafe_shouldClearAllSeats() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.makeReservation(A_RESERVATION_FOR_TWO);

        cafe.close();

        Layout layout = cafe.getLayout();
        int numberOfAvailableSeats = (int) layout.getCubes().stream().map(Cube::getSeats).flatMap(List::stream).filter(Seat::isCurrentlyAvailable).count();
        assertEquals(SOME_CUBE_NAMES.size() * A_DEFAULT_CONFIGURATION.cubeSize(), numberOfAvailableSeats);
    }

    @Test
    public void whenClosingCafe_shouldClearAllCustomers() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());

        cafe.close();

        assertThrows(CustomerNotFoundException.class, () -> cafe.getSeatByCustomerId(aCustomer.getId()));
    }

    @Test
    public void whenClosingCafe_shouldClearAllReservations() {
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.makeReservation(A_RESERVATION_FOR_TWO);

        cafe.close();

        assertTrue(cafe.getReservations().isEmpty());
    }

    @Test
    public void whenClosingCafe_shouldClearAllOrders() {
        Cafe cafe = cafeWithEnoughInventory();
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.placeOrder(aCustomer.getId(), new OrderFixture().build());

        cafe.close();
        cafe.checkIn(new CustomerFixture().withCustomerId(aCustomer.getId()).build(), Optional.empty());

        assertTrue(cafe.getOrderByCustomerId(aCustomer.getId()).items().isEmpty());
    }

    @Test
    public void whenClosingCafe_shouldClearInventory() {
        Cafe cafe = cafeWithEnoughInventory();

        cafe.close();

        assertTrue(cafe.getInventory().getIngredients().isEmpty());
    }

    @Test
    public void whenClosingCafe_shouldClearAllBills() {
        Cafe cafe = cafeWithEnoughInventory();
        Customer aCustomer = new CustomerFixture().build();
        cafe.checkIn(aCustomer, Optional.empty());
        cafe.placeOrder(aCustomer.getId(), new OrderFixture().build());
        cafe.checkOut(aCustomer.getId());

        cafe.close();
        cafe.checkIn(aCustomer, Optional.empty());

        assertThrows(CustomerNoBillException.class, () -> cafe.getCustomerBill(aCustomer.getId()));
    }

    private Cafe cafeWithEnoughInventory() {
        cafe.addIngredientsToInventory(
            Map.of(IngredientType.Milk, new Quantity(1000), IngredientType.Chocolate, new Quantity(1000), IngredientType.Water, new Quantity(1000),
                IngredientType.Espresso, new Quantity(1000)));
        return cafe;
    }
}
