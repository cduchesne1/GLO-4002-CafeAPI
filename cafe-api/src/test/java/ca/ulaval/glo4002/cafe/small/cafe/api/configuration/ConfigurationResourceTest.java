package ca.ulaval.glo4002.cafe.small.cafe.api.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.configuration.ConfigurationResource;
import ca.ulaval.glo4002.cafe.api.configuration.request.ConfigurationRequest;
import ca.ulaval.glo4002.cafe.application.CafeService;
import ca.ulaval.glo4002.cafe.application.parameter.ConfigurationParams;
import ca.ulaval.glo4002.cafe.fixture.request.ConfigurationRequestFixture;

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
        ConfigurationRequest configurationRequest = new ConfigurationRequestFixture()
            .withCubeSize(CUBE_SIZE)
            .withOrganizationName(ORGANISATION_NAME)
            .withGroupReservationMethod(GROUP_RESERVATION_METHOD)
            .withCountry(COUNTRY)
            .withProvince(PROVINCE)
            .withState(STATE)
            .withTipRate(GROUP_TIP_RATE)
            .build();
        ConfigurationParams configurationParams =
            new ConfigurationParams(CUBE_SIZE, ORGANISATION_NAME, GROUP_RESERVATION_METHOD, COUNTRY, PROVINCE, STATE, GROUP_TIP_RATE);

        configurationResource.updateConfiguration(configurationRequest);

        verify(cafeService).updateConfiguration(configurationParams);
    }

    @Test
    public void givenValidRequest_whenUpdatingConfiguration_shouldReturn200() {
        ConfigurationRequest configurationRequest = new ConfigurationRequestFixture()
            .withCubeSize(CUBE_SIZE)
            .withOrganizationName(ORGANISATION_NAME)
            .withGroupReservationMethod(GROUP_RESERVATION_METHOD)
            .withCountry(COUNTRY)
            .withProvince(PROVINCE)
            .withState(STATE)
            .withTipRate(GROUP_TIP_RATE)
            .build();

        Response response = configurationResource.updateConfiguration(configurationRequest);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
