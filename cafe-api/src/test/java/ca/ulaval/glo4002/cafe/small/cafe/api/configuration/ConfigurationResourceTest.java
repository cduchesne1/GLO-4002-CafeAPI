package ca.ulaval.glo4002.cafe.small.cafe.api.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.configuration.ConfigurationResource;
import ca.ulaval.glo4002.cafe.api.configuration.request.UpdateConfigurationRequest;
import ca.ulaval.glo4002.cafe.api.configuration.request.UpdateMenuRequest;
import ca.ulaval.glo4002.cafe.application.configuration.ConfigurationService;
import ca.ulaval.glo4002.cafe.application.configuration.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.application.configuration.query.UpdateMenuQuery;
import ca.ulaval.glo4002.cafe.application.inventory.query.IngredientsQuery;
import ca.ulaval.glo4002.cafe.fixture.request.UpdateConfigurationRequestFixture;
import ca.ulaval.glo4002.cafe.fixture.request.UpdateMenuRequestFixture;

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

    private ConfigurationService configurationService;
    private ConfigurationResource configurationResource;

    @BeforeEach
    public void createConfigurationResource() {
        configurationService = mock(ConfigurationService.class);
        configurationResource = new ConfigurationResource(configurationService);
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

        verify(configurationService).updateConfiguration(updateConfigurationQuery);
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

    @Test
    public void whenUpdatingMenu_shouldUpdateMenu() {
        UpdateMenuRequest updateMenuRequest = new UpdateMenuRequestFixture().build();
        IngredientsQuery ingredientsQuery =
            IngredientsQuery.from(updateMenuRequest.ingredients.Chocolate, updateMenuRequest.ingredients.Milk, updateMenuRequest.ingredients.Water,
                updateMenuRequest.ingredients.Espresso);
        UpdateMenuQuery updateMenuQuery = new UpdateMenuQuery(updateMenuRequest.name, ingredientsQuery, updateMenuRequest.cost);

        configurationResource.updateMenu(updateMenuRequest);

        verify(configurationService).updateMenu(updateMenuQuery);
    }

    @Test
    public void givenValidRequest_whenUpdatingMenu_shouldReturn200() {
        UpdateMenuRequest updateMenuRequest = new UpdateMenuRequestFixture().build();

        Response response = configurationResource.updateMenu(updateMenuRequest);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
