package ca.ulaval.glo4002.cafe.api;

import ca.ulaval.glo4002.cafe.api.layout.assembler.LayoutResponseAssembler;
import ca.ulaval.glo4002.cafe.api.layout.response.LayoutResponse;
import ca.ulaval.glo4002.cafe.service.CafeService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class CafeResource {
    private final CafeService cafeService;
    private final LayoutResponseAssembler layoutResponseAssembler = new LayoutResponseAssembler();

    public CafeResource(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @GET
    @Path("/layout")
    public Response layout() {
        LayoutResponse response = layoutResponseAssembler.toLayoutResponse(cafeService.getLayout());
        return Response.ok(response).build();
    }
}
