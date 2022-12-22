package ca.ulaval.glo4002.cafe.large;

import java.util.HashMap;
import java.util.Map;

import io.restassured.response.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.configuration.request.UpdateConfigurationRequest;
import ca.ulaval.glo4002.cafe.api.layout.SeatStatus;
import ca.ulaval.glo4002.cafe.api.layout.response.LayoutResponse;
import ca.ulaval.glo4002.cafe.api.reservation.request.ReservationRequest;
import ca.ulaval.glo4002.cafe.domain.reservation.ReservationType;
import ca.ulaval.glo4002.cafe.fixture.request.ReservationRequestFixture;
import ca.ulaval.glo4002.cafe.fixture.request.UpdateConfigurationRequestFixture;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationResourceEnd2EndTest {
    private static final String BASE_URL = "http://localhost:8181";
    private static final String AN_ORGANISATION_NAME = "MyLittleCafe";
    private static final int A_CUBE_SIZE = 5;
    private static final String A_VALID_GROUP_NAME = "test_group";

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
    public void givenValidConfigRequest_whenUpdatingConfig_shouldReturn200() {
        UpdateConfigurationRequest updateConfigurationRequest = new UpdateConfigurationRequestFixture().build();

        Response response = given().contentType("application/json").body(updateConfigurationRequest).when().post(BASE_URL + "/config");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void givenConfigRequestWithAdditionalFields_whenUpdatingConfig_shouldReturn200() {
        UpdateConfigurationRequest updateConfigurationRequest = new UpdateConfigurationRequestFixture().build();
        Map<String, Object> body = new HashMap<>();
        body.put("group_reservation_method", updateConfigurationRequest.group_reservation_method);
        body.put("organization_name", updateConfigurationRequest.organization_name);
        body.put("cube_size", updateConfigurationRequest.cube_size);
        body.put("country", updateConfigurationRequest.country);
        body.put("province", updateConfigurationRequest.province);
        body.put("state", updateConfigurationRequest.state);
        body.put("additional_field", "additional_value");

        Response response = given().contentType("application/json").body(body).when().post(BASE_URL + "/config");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void givenValidConfigRequest_whenUpdatingConfig_shouldSetNewOrganisationName() {
        UpdateConfigurationRequest updateConfigurationRequest =
            new UpdateConfigurationRequestFixture().withOrganizationName(AN_ORGANISATION_NAME).withCubeSize(A_CUBE_SIZE)
                .withGroupReservationMethod(ReservationType.FullCubes.toString()).build();
        given().contentType("application/json").body(updateConfigurationRequest).when().post(BASE_URL + "/config");

        Response response = when().get(BASE_URL + "/layout");
        LayoutResponse actualBody = response.getBody().as(LayoutResponse.class);

        assertEquals(AN_ORGANISATION_NAME, actualBody.name());
    }

    @Test
    public void givenValidConfigRequest_whenUpdatingConfig_shouldSetNewCubeSize() {
        UpdateConfigurationRequest updateConfigurationRequest =
            new UpdateConfigurationRequestFixture().withOrganizationName(AN_ORGANISATION_NAME).withCubeSize(A_CUBE_SIZE)
                .withGroupReservationMethod(ReservationType.FullCubes.toString()).build();
        given().contentType("application/json").body(updateConfigurationRequest).when().post(BASE_URL + "/config");

        Response response = when().get(BASE_URL + "/layout");
        LayoutResponse actualBody = response.getBody().as(LayoutResponse.class);

        assertEquals(A_CUBE_SIZE, actualBody.cubes().get(0).seats().size());
    }

    @Test
    public void givenValidConfigRequest_whenUpdatingConfig_shouldResetCafeWithNewStrategy() {
        UpdateConfigurationRequest updateConfigurationRequest =
            new UpdateConfigurationRequestFixture().withOrganizationName(AN_ORGANISATION_NAME).withCubeSize(A_CUBE_SIZE)
                .withGroupReservationMethod(ReservationType.FullCubes.toString()).build();
        given().contentType("application/json").body(updateConfigurationRequest).when().post(BASE_URL + "/config");
        postReservationWithGroupName(A_VALID_GROUP_NAME);

        Response response = when().get(BASE_URL + "/layout");
        LayoutResponse actualBody = response.getBody().as(LayoutResponse.class);

        assertEquals(SeatStatus.Reserved, actualBody.cubes().get(0).seats().get(0).status());
        assertEquals(SeatStatus.Reserved, actualBody.cubes().get(0).seats().get(1).status());
        assertEquals(SeatStatus.Reserved, actualBody.cubes().get(0).seats().get(2).status());
        assertEquals(SeatStatus.Reserved, actualBody.cubes().get(0).seats().get(3).status());
        assertEquals(SeatStatus.Reserved, actualBody.cubes().get(0).seats().get(4).status());
    }

    private void postReservationWithGroupName(String groupName) {
        ReservationRequest reservationRequest = (new ReservationRequestFixture()).withGroupName(groupName).withGroupSize(2).build();
        given().contentType("application/json").body(reservationRequest).when().post(BASE_URL + "/reservations");
    }
}
