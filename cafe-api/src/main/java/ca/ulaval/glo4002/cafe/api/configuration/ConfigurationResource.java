package ca.ulaval.glo4002.cafe.api.configuration;

import ca.ulaval.glo4002.cafe.api.configuration.request.UpdateConfigurationRequest;
import ca.ulaval.glo4002.cafe.api.configuration.request.UpdateMenuRequest;
import ca.ulaval.glo4002.cafe.application.configuration.ConfigurationService;
import ca.ulaval.glo4002.cafe.application.configuration.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.application.configuration.query.UpdateMenuQuery;
import ca.ulaval.glo4002.cafe.application.inventory.query.IngredientsQuery;

import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {
    private final ConfigurationService configurationService;

    public ConfigurationResource(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @POST
    @Path("/config")
    public Response updateConfiguration(@Valid UpdateConfigurationRequest updateConfigurationRequest) {
        UpdateConfigurationQuery updateConfigurationQuery =
            UpdateConfigurationQuery.from(updateConfigurationRequest.cube_size, updateConfigurationRequest.organization_name,
                updateConfigurationRequest.group_reservation_method, updateConfigurationRequest.country, updateConfigurationRequest.province,
                updateConfigurationRequest.state, updateConfigurationRequest.group_tip_rate);
        configurationService.updateConfiguration(updateConfigurationQuery);
        return Response.ok().build();
    }

    @POST
    @Path("/menu")
    public Response updateMenu(@Valid UpdateMenuRequest updateMenuRequest) {
        IngredientsQuery ingredientsQuery =
            IngredientsQuery.from(updateMenuRequest.ingredients.Chocolate, updateMenuRequest.ingredients.Milk, updateMenuRequest.ingredients.Water,
                updateMenuRequest.ingredients.Espresso);
        UpdateMenuQuery updateMenuQuery = UpdateMenuQuery.from(updateMenuRequest.name, ingredientsQuery, updateMenuRequest.cost);
        configurationService.updateMenu(updateMenuQuery);
        return Response.ok().build();
    }
}
