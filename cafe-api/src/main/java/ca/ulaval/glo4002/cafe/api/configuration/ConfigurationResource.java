package ca.ulaval.glo4002.cafe.api.configuration;

import ca.ulaval.glo4002.cafe.api.configuration.request.UpdateConfigurationRequest;
import ca.ulaval.glo4002.cafe.application.CafeService;
import ca.ulaval.glo4002.cafe.application.query.UpdateConfigurationQuery;

import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {
    private final CafeService cafeService;

    public ConfigurationResource(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @POST
    @Path("/config")
    public Response updateConfiguration(@Valid UpdateConfigurationRequest updateConfigurationRequest) {
        UpdateConfigurationQuery updateConfigurationQuery =
            UpdateConfigurationQuery.from(updateConfigurationRequest.cube_size, updateConfigurationRequest.organization_name,
                updateConfigurationRequest.group_reservation_method, updateConfigurationRequest.country, updateConfigurationRequest.province,
                updateConfigurationRequest.state, updateConfigurationRequest.group_tip_rate);
        cafeService.updateConfiguration(updateConfigurationQuery);
        return Response.ok().build();
    }
}
