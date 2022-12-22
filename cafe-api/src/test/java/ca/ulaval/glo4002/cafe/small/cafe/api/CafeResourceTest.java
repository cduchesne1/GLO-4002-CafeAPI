package ca.ulaval.glo4002.cafe.small.cafe.api;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.CafeResource;
import ca.ulaval.glo4002.cafe.api.request.CheckInRequest;
import ca.ulaval.glo4002.cafe.api.request.CheckOutRequest;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.fixture.request.CheckInRequestFixture;
import ca.ulaval.glo4002.cafe.fixture.request.CheckOutRequestFixture;
import ca.ulaval.glo4002.cafe.service.CafeService;
import ca.ulaval.glo4002.cafe.service.customer.CustomerService;
import ca.ulaval.glo4002.cafe.service.customer.parameter.CheckInCustomerParams;
import ca.ulaval.glo4002.cafe.service.customer.parameter.CheckOutCustomerParams;
import ca.ulaval.glo4002.cafe.service.dto.LayoutDTO;

import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CafeResourceTest {
    private static final CafeName A_CAFE_NAME = new CafeName("Bob");
    private static final LayoutDTO A_LAYOUT_DTO = new LayoutDTO(A_CAFE_NAME, new ArrayList<>());
    private static final String CUSTOMER_ID = "customerId";
    private static final String CUSTOMER_NAME = "Bob";
    private static final String GROUP_NAME = "team";

    private CafeService cafeService;
    private CustomerService customerService;
    private CafeResource cafeResource;

    @BeforeEach
    public void createCafeResource() {
        cafeService = mock(CafeService.class);
        customerService = mock(CustomerService.class);
        cafeResource = new CafeResource(cafeService, customerService);
    }

    @Test
    public void whenGettingLayout_shouldGetLayout() {
        when(cafeService.getLayout()).thenReturn(A_LAYOUT_DTO);

        cafeResource.layout();

        verify(cafeService).getLayout();
    }

    @Test
    public void whenGettingLayout_shouldReturn200() {
        when(cafeService.getLayout()).thenReturn(A_LAYOUT_DTO);

        Response response = cafeResource.layout();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void whenClosing_shouldCloseCafe() {
        cafeResource.close();

        verify(cafeService).closeCafe();
    }

    @Test
    public void whenClosing_shouldReturn200() {
        Response response = cafeResource.close();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void whenCheckingIn_shouldCheckInCustomer() {
        CheckInRequest checkInRequest = new CheckInRequestFixture()
            .withCustomerId(CUSTOMER_ID)
            .withCustomerName(CUSTOMER_NAME)
            .withGroupName(GROUP_NAME)
            .build();
        CheckInCustomerParams checkInCustomerParams = new CheckInCustomerParams(CUSTOMER_ID, CUSTOMER_NAME, GROUP_NAME);

        cafeResource.checkIn(checkInRequest);

        verify(customerService).checkIn(checkInCustomerParams);
    }

    @Test
    public void givenValidRequest_whenCheckingIn_shouldReturn201() {
        CheckInRequest checkInRequest = new CheckInRequestFixture()
            .withCustomerId(CUSTOMER_ID)
            .withCustomerName(CUSTOMER_NAME)
            .withGroupName(GROUP_NAME)
            .build();

        Response response = cafeResource.checkIn(checkInRequest);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void whenCheckingIn_shouldReturnPathToCustomerInLocation() {
        CheckInRequest checkInRequest = new CheckInRequestFixture()
            .withCustomerId(CUSTOMER_ID)
            .withCustomerName(CUSTOMER_NAME)
            .withGroupName(GROUP_NAME)
            .build();

        Response response = cafeResource.checkIn(checkInRequest);

        assertTrue(response.getLocation().toString().contains("/customers/" + CUSTOMER_ID));
    }

    @Test
    public void whenCheckingOut_shouldCheckOutCustomer() {
        CheckOutRequest checkOutRequest = new CheckOutRequestFixture()
            .withCustomerId(CUSTOMER_ID)
            .build();
        CheckOutCustomerParams checkOutCustomerParams = new CheckOutCustomerParams(CUSTOMER_ID);

        cafeResource.checkOut(checkOutRequest);

        verify(customerService).checkOut(checkOutCustomerParams);
    }

    @Test
    public void givenValidRequest_whenCheckingOut_shouldReturn201() {
        CheckOutRequest checkOutRequest = new CheckOutRequestFixture()
            .withCustomerId(CUSTOMER_ID)
            .build();

        Response response = cafeResource.checkOut(checkOutRequest);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void whenCheckingOut_shouldReturnPathToBillInLocation() {
        CheckOutRequest checkOutRequest = new CheckOutRequestFixture()
            .withCustomerId(CUSTOMER_ID)
            .build();

        Response response = cafeResource.checkOut(checkOutRequest);

        assertTrue(response.getLocation().toString().contains("/customers/" + CUSTOMER_ID + "/bill"));
    }
}
