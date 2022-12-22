package ca.ulaval.glo4002.cafe.small.cafe.api.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.operation.OperationResource;
import ca.ulaval.glo4002.cafe.api.operation.request.CheckInRequest;
import ca.ulaval.glo4002.cafe.api.operation.request.CheckOutRequest;
import ca.ulaval.glo4002.cafe.application.CafeService;
import ca.ulaval.glo4002.cafe.application.customer.CustomerService;
import ca.ulaval.glo4002.cafe.application.customer.query.CheckInCustomerQuery;
import ca.ulaval.glo4002.cafe.application.customer.query.CheckOutCustomerQuery;
import ca.ulaval.glo4002.cafe.fixture.request.CheckInRequestFixture;
import ca.ulaval.glo4002.cafe.fixture.request.CheckOutRequestFixture;

import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OperationResourceTest {
    private static final String CUSTOMER_ID = "customerId";
    private static final String CUSTOMER_NAME = "Bob";
    private static final String GROUP_NAME = "team";

    private CafeService cafeService;
    private CustomerService customerService;
    private OperationResource operationResource;

    @BeforeEach
    public void createOperationResource() {
        cafeService = mock(CafeService.class);
        customerService = mock(CustomerService.class);
        operationResource = new OperationResource(cafeService, customerService);
    }

    @Test
    public void whenClosing_shouldCloseCafe() {
        operationResource.close();

        verify(cafeService).closeCafe();
    }

    @Test
    public void whenClosing_shouldReturn200() {
        Response response = operationResource.close();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void whenCheckingIn_shouldCheckInCustomer() {
        CheckInRequest checkInRequest =
            new CheckInRequestFixture().withCustomerId(CUSTOMER_ID).withCustomerName(CUSTOMER_NAME).withGroupName(GROUP_NAME).build();
        CheckInCustomerQuery checkInCustomerQuery = new CheckInCustomerQuery(CUSTOMER_ID, CUSTOMER_NAME, GROUP_NAME);

        operationResource.checkIn(checkInRequest);

        verify(customerService).checkIn(checkInCustomerQuery);
    }

    @Test
    public void givenValidRequest_whenCheckingIn_shouldReturn201() {
        CheckInRequest checkInRequest =
            new CheckInRequestFixture().withCustomerId(CUSTOMER_ID).withCustomerName(CUSTOMER_NAME).withGroupName(GROUP_NAME).build();

        Response response = operationResource.checkIn(checkInRequest);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void whenCheckingIn_shouldReturnPathToCustomerInLocation() {
        CheckInRequest checkInRequest =
            new CheckInRequestFixture().withCustomerId(CUSTOMER_ID).withCustomerName(CUSTOMER_NAME).withGroupName(GROUP_NAME).build();

        Response response = operationResource.checkIn(checkInRequest);

        assertTrue(response.getLocation().toString().contains("/customers/" + CUSTOMER_ID));
    }

    @Test
    public void whenCheckingOut_shouldCheckOutCustomer() {
        CheckOutRequest checkOutRequest = new CheckOutRequestFixture().withCustomerId(CUSTOMER_ID).build();
        CheckOutCustomerQuery checkOutCustomerQuery = new CheckOutCustomerQuery(CUSTOMER_ID);

        operationResource.checkOut(checkOutRequest);

        verify(customerService).checkOut(checkOutCustomerQuery);
    }

    @Test
    public void givenValidRequest_whenCheckingOut_shouldReturn201() {
        CheckOutRequest checkOutRequest = new CheckOutRequestFixture().withCustomerId(CUSTOMER_ID).build();

        Response response = operationResource.checkOut(checkOutRequest);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void whenCheckingOut_shouldReturnPathToBillInLocation() {
        CheckOutRequest checkOutRequest = new CheckOutRequestFixture().withCustomerId(CUSTOMER_ID).build();

        Response response = operationResource.checkOut(checkOutRequest);

        assertTrue(response.getLocation().toString().contains("/customers/" + CUSTOMER_ID + "/bill"));
    }
}
