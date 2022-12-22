package ca.ulaval.glo4002.cafe.medium;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.CafeService;
import ca.ulaval.glo4002.cafe.application.customer.CustomerService;
import ca.ulaval.glo4002.cafe.application.customer.payload.BillPayload;
import ca.ulaval.glo4002.cafe.application.customer.payload.CustomerPayload;
import ca.ulaval.glo4002.cafe.application.customer.payload.OrderPayload;
import ca.ulaval.glo4002.cafe.application.customer.query.CheckInCustomerQuery;
import ca.ulaval.glo4002.cafe.application.customer.query.CheckOutCustomerQuery;
import ca.ulaval.glo4002.cafe.application.customer.query.CustomerOrderQuery;
import ca.ulaval.glo4002.cafe.application.query.IngredientsQuery;
import ca.ulaval.glo4002.cafe.domain.CafeFactory;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerNotFoundException;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.SeatNumber;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.Amount;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerFactory;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerId;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerName;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.order.Order;
import ca.ulaval.glo4002.cafe.fixture.OrderFixture;
import ca.ulaval.glo4002.cafe.infrastructure.InMemoryCafeRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerServiceTest {
    private static final CustomerId A_CUSTOMER_ID = new CustomerId("123");
    private static final String A_CUSTOMER_NAME = "Joe";
    private static final CheckInCustomerQuery CHECK_IN_CUSTOMER_QUERY = new CheckInCustomerQuery(A_CUSTOMER_ID.value(), A_CUSTOMER_NAME, null);
    private static final CheckOutCustomerQuery CHECK_OUT_CUSTOMER_QUERY = new CheckOutCustomerQuery(A_CUSTOMER_ID.value());
    private static final CustomerOrderQuery CUSTOMER_ORDER_QUERY = new CustomerOrderQuery(A_CUSTOMER_ID, new OrderFixture().build());
    private static final IngredientsQuery INGREDIENTS_QUERY = new IngredientsQuery(100, 100, 100, 100);

    CustomerService customerService;
    CafeRepository cafeRepository;
    CafeService cafeService;

    @BeforeEach
    public void instanciateAttributes() {
        cafeRepository = new InMemoryCafeRepository();
        customerService = new CustomerService(cafeRepository, new CustomerFactory());
        cafeService = new CafeService(cafeRepository, new CafeFactory());
        cafeService.initializeCafe();
    }

    @Test
    public void whenCheckingIn_shouldSaveCustomer() {
        customerService.checkIn(CHECK_IN_CUSTOMER_QUERY);

        assertDoesNotThrow(() -> customerService.getCustomer(A_CUSTOMER_ID));
    }

    @Test
    public void givenSavedCustomer_whenCheckingOut_shouldSaveCustomerBill() {
        customerService.checkIn(CHECK_IN_CUSTOMER_QUERY);

        customerService.checkOut(CHECK_OUT_CUSTOMER_QUERY);

        assertNotNull(customerService.getCustomerBill(A_CUSTOMER_ID));
    }

    @Test
    public void givenSavedCustomer_whenCheckingOut_shouldRemoveCustomerFromSeat() {
        customerService.checkIn(CHECK_IN_CUSTOMER_QUERY);

        customerService.checkOut(CHECK_OUT_CUSTOMER_QUERY);

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomer(A_CUSTOMER_ID));
    }

    @Test
    public void givenSavedCustomer_whenPlacingOrder_shouldSaveOrderForCustomer() {
        Order expectedOrder = new OrderFixture().build();
        customerService.checkIn(CHECK_IN_CUSTOMER_QUERY);
        cafeService.addIngredientsToInventory(INGREDIENTS_QUERY);

        customerService.placeOrder(CUSTOMER_ORDER_QUERY);
        OrderPayload actualOrder = customerService.getOrder(A_CUSTOMER_ID);

        assertEquals(OrderPayload.fromOrder(expectedOrder), actualOrder);
    }

    @Test
    public void givenSavedCustomer_whenGettingCustomer_shouldReturnValidCustomerPayload() {
        CustomerPayload expectedCustomerPayload = new CustomerPayload(new CustomerName(A_CUSTOMER_NAME), new SeatNumber(1), Optional.empty());
        customerService.checkIn(CHECK_IN_CUSTOMER_QUERY);

        CustomerPayload actualCustomerPayload = customerService.getCustomer(A_CUSTOMER_ID);

        assertEquals(expectedCustomerPayload, actualCustomerPayload);
    }

    @Test
    public void givenSavedCustomerWithOrder_whenGettingOrder_shouldReturnValidOrderPayload() {
        OrderPayload expectedOrderPayload = new OrderPayload(CUSTOMER_ORDER_QUERY.order().items());
        customerService.checkIn(CHECK_IN_CUSTOMER_QUERY);
        cafeService.addIngredientsToInventory(INGREDIENTS_QUERY);
        customerService.placeOrder(CUSTOMER_ORDER_QUERY);

        OrderPayload actualOrderPayload = customerService.getOrder(CHECK_IN_CUSTOMER_QUERY.customerId());

        assertEquals(expectedOrderPayload, actualOrderPayload);
    }

    @Test
    public void givenSavedBill_whenGettingCustomerBill_shouldReturnValidBillPayload() {
        BillPayload expectedBillPayload =
            new BillPayload(new LinkedList<>(CUSTOMER_ORDER_QUERY.order().items()), new Amount(0), new Amount(2.25f), new Amount(0), new Amount(2.25f));
        customerService.checkIn(CHECK_IN_CUSTOMER_QUERY);
        cafeService.addIngredientsToInventory(INGREDIENTS_QUERY);
        customerService.placeOrder(CUSTOMER_ORDER_QUERY);
        customerService.checkOut(CHECK_OUT_CUSTOMER_QUERY);

        BillPayload actualBillPayload = customerService.getCustomerBill(CHECK_IN_CUSTOMER_QUERY.customerId());

        assertEquals(expectedBillPayload, actualBillPayload);
    }
}
