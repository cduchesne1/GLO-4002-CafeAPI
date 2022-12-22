package ca.ulaval.glo4002.cafe.large;

import io.restassured.response.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.operation.request.CheckInRequest;
import ca.ulaval.glo4002.cafe.api.operation.request.CheckOutRequest;
import ca.ulaval.glo4002.cafe.api.reservation.request.ReservationRequest;
import ca.ulaval.glo4002.cafe.fixture.request.CheckInRequestFixture;
import ca.ulaval.glo4002.cafe.fixture.request.ReservationRequestFixture;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationResourceEnd2EndTest {
    private static final String BASE_URL = "http://localhost:8181";
    private static final String A_VALID_ID = "test_ID";
    private static final CheckInRequest A_VALID_CHECK_IN_REQUEST = new CheckInRequestFixture().withCustomerId(A_VALID_ID).build();

    private TestServer server;

    @BeforeEach
    public void startServer() throws Exception {
        server = new TestServer();
        server.start();
    }

    @AfterEach
    public void closeServer() throws Exception {
        server.stop();
    }

    @Test
    public void whenClosing_shouldReturn200() {
        Response response = when().post(BASE_URL + "/close");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void givenAGroup_whenCheckingIn_shouldReturn201() {
        String groupName = "a_group";
        postReservationWithGroupName(groupName);
        CheckInRequest checkInRequest = new CheckInRequestFixture().withGroupName(groupName).build();

        Response response = given().contentType("application/json").body(checkInRequest).when().post(BASE_URL + "/check-in");

        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void givenNoGroup_whenCheckingIn_shouldReturn201() {
        CheckInRequest checkInRequest = new CheckInRequestFixture().build();

        Response response = given().contentType("application/json").body(checkInRequest).when().post(BASE_URL + "/check-in");

        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void whenCheckingIn_shouldReturnWithLocationHeader() {
        CheckInRequest checkInRequest = new CheckInRequestFixture().withCustomerId(A_VALID_ID).build();

        Response response = given().contentType("application/json").body(checkInRequest).when().post(BASE_URL + "/check-in");

        assertEquals(BASE_URL + "/customers/" + A_VALID_ID, response.header("Location"));
    }

    @Test
    public void whenCheckingOut_shouldReturn201() {
        postCheckInWithCheckInRequest(A_VALID_CHECK_IN_REQUEST);
        CheckOutRequest checkOutRequest = new CheckOutRequest();
        checkOutRequest.customer_id = A_VALID_ID;

        Response response = given().contentType("application/json").body(checkOutRequest).when().post(BASE_URL + "/checkout");

        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void whenCheckingOut_shouldReturnBillLocationHeader() {
        postCheckInWithCheckInRequest(A_VALID_CHECK_IN_REQUEST);
        CheckOutRequest checkOutRequest = new CheckOutRequest();
        checkOutRequest.customer_id = A_VALID_ID;

        Response response = given().contentType("application/json").body(checkOutRequest).when().post(BASE_URL + "/checkout");

        assertEquals(BASE_URL + "/customers/" + A_VALID_ID + "/bill", response.getHeader("Location"));
    }

    private void postCheckInWithCheckInRequest(CheckInRequest checkInRequest) {
        given().contentType("application/json").body(checkInRequest).when().post(BASE_URL + "/check-in");
    }

    private void postReservationWithGroupName(String groupName) {
        ReservationRequest reservationRequest = (new ReservationRequestFixture()).withGroupName(groupName).withGroupSize(2).build();
        given().contentType("application/json").body(reservationRequest).when().post(BASE_URL + "/reservations");
    }
}
