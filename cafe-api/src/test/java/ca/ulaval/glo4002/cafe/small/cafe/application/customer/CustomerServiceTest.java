package ca.ulaval.glo4002.cafe.small.cafe.application.customer;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.customer.CustomerService;
import ca.ulaval.glo4002.cafe.application.customer.payload.BillPayload;
import ca.ulaval.glo4002.cafe.application.customer.payload.CustomerPayload;
import ca.ulaval.glo4002.cafe.application.customer.payload.OrderPayload;
import ca.ulaval.glo4002.cafe.application.customer.query.CheckInCustomerQuery;
import ca.ulaval.glo4002.cafe.application.customer.query.CheckOutCustomerQuery;
import ca.ulaval.glo4002.cafe.application.customer.query.CustomerOrderQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.bill.Bill;
import ca.ulaval.glo4002.cafe.domain.bill.BillFactory;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.Seat;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.SeatNumber;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.Customer;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerFactory;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerId;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerName;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.order.Order;
import ca.ulaval.glo4002.cafe.fixture.BillFixture;
import ca.ulaval.glo4002.cafe.fixture.CustomerFixture;
import ca.ulaval.glo4002.cafe.fixture.OrderFixture;
import ca.ulaval.glo4002.cafe.fixture.SeatFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {
    private static final CustomerName CUSTOMER_NAME = new CustomerName("Bob Bisonette");
    private static final CustomerId CUSTOMER_ID = new CustomerId("ABC273031");
    private static final CheckInCustomerQuery CHECK_IN_CUSTOMER_QUERY_NO_GROUP = new CheckInCustomerQuery(CUSTOMER_ID.value(), CUSTOMER_NAME.value(), null);
    private static final CheckOutCustomerQuery CHECK_OUT_CUSTOMER_QUERY = new CheckOutCustomerQuery(CUSTOMER_ID.value());
    private static final Customer CUSTOMER = new CustomerFixture().withCustomerId(CUSTOMER_ID).withCustomerName(CUSTOMER_NAME).build();
    private static final Seat SEAT_WITH_CUSTOMER = new SeatFixture().withSeatNumber(new SeatNumber(1)).withCustomer(CUSTOMER).build();
    private static final CoffeeName AN_AMERICANO_COFFEE = new CoffeeName("Americano");
    private static final CoffeeName A_DARK_ROAST_COFFEE = new CoffeeName("Dark Roast");
    private static final Order A_ORDER = new OrderFixture().build();
    private static final Bill A_VALID_BILL = new BillFixture().build();

    private CustomerService customersService;
    private CustomerFactory customerFactory;
    private BillFactory billFactory;

    private CafeRepository cafeRepository;
    private Cafe mockCafe;

    @BeforeEach
    public void createCustomersService() {
        cafeRepository = mock(CafeRepository.class);
        customerFactory = mock(CustomerFactory.class);
        billFactory = mock(BillFactory.class);
        customersService = new CustomerService(cafeRepository, customerFactory, billFactory);

        mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);
    }

    @Test
    public void whenGettingCustomer_shouldGetCafe() {
        when(mockCafe.getSeatByCustomerId(CUSTOMER_ID)).thenReturn(SEAT_WITH_CUSTOMER);

        customersService.getCustomer(CUSTOMER_ID);

        verify(cafeRepository).get();
    }

    @Test
    public void whenGettingCustomer_shouldGetCustomerSeat() {
        when(mockCafe.getSeatByCustomerId(CUSTOMER_ID)).thenReturn(SEAT_WITH_CUSTOMER);

        customersService.getCustomer(CUSTOMER_ID);

        verify(mockCafe).getSeatByCustomerId(CUSTOMER_ID);
    }

    @Test
    public void whenGettingCustomer_shouldReturnMatchingCustomerPayload() {
        when(mockCafe.getSeatByCustomerId(CUSTOMER_ID)).thenReturn(SEAT_WITH_CUSTOMER);

        CustomerPayload actualCustomerPayload = customersService.getCustomer(CUSTOMER_ID);

        assertEquals(CUSTOMER_NAME, actualCustomerPayload.name());
        assertEquals(SEAT_WITH_CUSTOMER.getNumber(), actualCustomerPayload.seatNumber());
    }

    @Test
    public void whenCheckingInCustomer_shouldGetCafe() {
        when(customerFactory.createCustomer(CUSTOMER_ID, CUSTOMER_NAME)).thenReturn(CUSTOMER);

        customersService.checkIn(CHECK_IN_CUSTOMER_QUERY_NO_GROUP);

        verify(cafeRepository).get();
    }

    @Test
    public void whenCheckingInCustomer_shouldCreateNewCustomer() {
        when(customerFactory.createCustomer(CUSTOMER_ID, CUSTOMER_NAME)).thenReturn(CUSTOMER);

        customersService.checkIn(CHECK_IN_CUSTOMER_QUERY_NO_GROUP);

        verify(customerFactory).createCustomer(CUSTOMER_ID, CUSTOMER_NAME);
    }

    @Test
    public void whenCheckingInCustomer_shouldCheckInInCafe() {
        when(customerFactory.createCustomer(CUSTOMER_ID, CUSTOMER_NAME)).thenReturn(CUSTOMER);

        customersService.checkIn(CHECK_IN_CUSTOMER_QUERY_NO_GROUP);

        verify(mockCafe).checkIn(CUSTOMER, CHECK_IN_CUSTOMER_QUERY_NO_GROUP.groupName());
    }

    @Test
    public void whenCheckingInCustomer_shouldUpdateCafe() {
        when(customerFactory.createCustomer(CUSTOMER_ID, CUSTOMER_NAME)).thenReturn(CUSTOMER);

        customersService.checkIn(CHECK_IN_CUSTOMER_QUERY_NO_GROUP);

        verify(cafeRepository).saveOrUpdate(mockCafe);
    }

    @Test
    public void whenGettingOrders_shouldGetCafe() {
        when(mockCafe.getOrderByCustomerId(CUSTOMER_ID)).thenReturn(A_ORDER);

        customersService.getOrder(CUSTOMER_ID);

        verify(cafeRepository).get();
    }

    @Test
    public void whenGettingOrders_shouldGetOrdersFromCafe() {
        when(mockCafe.getOrderByCustomerId(CUSTOMER_ID)).thenReturn(A_ORDER);

        customersService.getOrder(CUSTOMER_ID);

        verify(mockCafe).getOrderByCustomerId(CUSTOMER_ID);
    }

    @Test
    public void whenGettingOrders_shouldReturnMatchingOrderPayload() {
        Order expectedOrders = new OrderFixture().withItems(List.of(AN_AMERICANO_COFFEE, A_DARK_ROAST_COFFEE)).build();
        when(mockCafe.getOrderByCustomerId(CUSTOMER_ID)).thenReturn(expectedOrders);

        OrderPayload actualOrderPayload = customersService.getOrder(CUSTOMER_ID);

        assertEquals(expectedOrders.items().get(0), actualOrderPayload.coffees().get(0));
        assertEquals(expectedOrders.items().get(1), actualOrderPayload.coffees().get(1));
    }

    @Test
    public void whenCheckingOut_shouldGetCafe() {
        customersService.checkOut(CHECK_OUT_CUSTOMER_QUERY);

        verify(cafeRepository).get();
    }

    @Test
    public void whenCheckingOut_shouldCheckOutWithCustomerId() {
        customersService.checkOut(CHECK_OUT_CUSTOMER_QUERY);

        verify(mockCafe).checkOut(CUSTOMER_ID);
    }

    @Test
    public void whenCheckingOut_shouldUpdateCafe() {
        customersService.checkOut(CHECK_OUT_CUSTOMER_QUERY);

        verify(cafeRepository).saveOrUpdate(mockCafe);
    }

    @Test
    public void whenPlacingOrder_shouldGetCafe() {
        customersService.placeOrder(new CustomerOrderQuery(CUSTOMER_ID.value(), List.of("Americano")));

        verify(cafeRepository).get();
    }

    @Test
    public void whenPlacingOrder_shouldPlaceOrder() {
        customersService.placeOrder(new CustomerOrderQuery(CUSTOMER_ID.value(), List.of("Americano")));

        verify(mockCafe).placeOrder(CUSTOMER_ID, new OrderFixture().withItems(List.of(AN_AMERICANO_COFFEE)).build());
    }

    @Test
    public void whenPlacingOrder_shouldUpdateCafe() {
        customersService.placeOrder(new CustomerOrderQuery(CUSTOMER_ID.value(), List.of("Americano")));

        verify(cafeRepository).saveOrUpdate(mockCafe);
    }

    @Test
    public void givenCheckedOutCustomer_whenGettingBill_shouldGetBillFromCafe() {
        when(mockCafe.getCustomerBill(CUSTOMER_ID)).thenReturn(A_VALID_BILL);
        customersService.getCustomerBill(CUSTOMER_ID);

        verify(mockCafe).getCustomerBill(CUSTOMER_ID);
    }

    @Test
    public void whenGettingBill_shouldGetCafe() {
        when(mockCafe.getCustomerBill(CUSTOMER_ID)).thenReturn(A_VALID_BILL);
        customersService.getCustomerBill(CUSTOMER_ID);

        verify(cafeRepository).get();
    }

    @Test
    public void whenGettingBill_shouldReturnMatchingBillPayload() {
        when(mockCafe.getCustomerBill(CUSTOMER_ID)).thenReturn(A_VALID_BILL);

        BillPayload actualBillPayload = customersService.getCustomerBill(CUSTOMER_ID);

        assertEquals(A_VALID_BILL.order().items(), actualBillPayload.coffees());
        assertEquals(A_VALID_BILL.subtotal(), actualBillPayload.subtotal());
        assertEquals(A_VALID_BILL.taxes(), actualBillPayload.taxes());
        assertEquals(A_VALID_BILL.total(), actualBillPayload.total());
        assertEquals(A_VALID_BILL.tip(), actualBillPayload.tip());
    }
}
