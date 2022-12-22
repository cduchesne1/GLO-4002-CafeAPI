package ca.ulaval.glo4002.cafe.api.configuration;

import ca.ulaval.glo4002.cafe.api.configuration.request.ConfigurationRequest;
import ca.ulaval.glo4002.cafe.service.CafeService;
import ca.ulaval.glo4002.cafe.service.parameter.ConfigurationParams;

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
    public Response updateConfiguration(@Valid ConfigurationRequest configurationRequest) {
        ConfigurationParams configurationParams =
            ConfigurationParams.from(configurationRequest.cube_size, configurationRequest.organization_name, configurationRequest.group_reservation_method,
                configurationRequest.country, configurationRequest.province, configurationRequest.state, configurationRequest.group_tip_rate);
        cafeService.updateConfiguration(configurationParams);
        return Response.ok().build();
    }
}
