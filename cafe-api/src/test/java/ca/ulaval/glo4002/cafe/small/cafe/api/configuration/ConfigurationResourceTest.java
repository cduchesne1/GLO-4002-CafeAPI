package ca.ulaval.glo4002.cafe.small.cafe.api.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.configuration.ConfigurationResource;
import ca.ulaval.glo4002.cafe.api.configuration.request.UpdateConfigurationRequest;
import ca.ulaval.glo4002.cafe.application.CafeService;
import ca.ulaval.glo4002.cafe.application.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.fixture.request.UpdateConfigurationRequestFixture;

import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConfigurationResourceTest {
    private static final int CUBE_SIZE = 1;
    private static final String ORGANISATION_NAME = "Bob";
    private static final String GROUP_RESERVATION_METHOD = "Default";
    private static final String COUNTRY = "CA";
    private static final String PROVINCE = "QC";
    private static final String STATE = "";
    private static final int GROUP_TIP_RATE = 0;

    private CafeService cafeService;
    private ConfigurationResource configurationResource;

    @BeforeEach
    public void createConfigurationResource() {
        cafeService = mock(CafeService.class);
        configurationResource = new ConfigurationResource(cafeService);
    }

    @Test
    public void whenUpdatingConfiguration_shouldUpdateConfiguration() {
        UpdateConfigurationRequest updateConfigurationRequest =
            new UpdateConfigurationRequestFixture().withCubeSize(CUBE_SIZE).withOrganizationName(ORGANISATION_NAME)
                .withGroupReservationMethod(GROUP_RESERVATION_METHOD).withCountry(COUNTRY).withProvince(PROVINCE).withState(STATE).withTipRate(GROUP_TIP_RATE)
                .build();
        UpdateConfigurationQuery updateConfigurationQuery =
            new UpdateConfigurationQuery(CUBE_SIZE, ORGANISATION_NAME, GROUP_RESERVATION_METHOD, COUNTRY, PROVINCE, STATE, GROUP_TIP_RATE);

        configurationResource.updateConfiguration(updateConfigurationRequest);

        verify(cafeService).updateConfiguration(updateConfigurationQuery);
    }

    @Test
    public void givenValidRequest_whenUpdatingConfiguration_shouldReturn200() {
        UpdateConfigurationRequest updateConfigurationRequest =
            new UpdateConfigurationRequestFixture().withCubeSize(CUBE_SIZE).withOrganizationName(ORGANISATION_NAME)
                .withGroupReservationMethod(GROUP_RESERVATION_METHOD).withCountry(COUNTRY).withProvince(PROVINCE).withState(STATE).withTipRate(GROUP_TIP_RATE)
                .build();

        Response response = configurationResource.updateConfiguration(updateConfigurationRequest);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
