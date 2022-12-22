package ca.ulaval.glo4002.cafe.api.operation;

import java.net.URI;

import ca.ulaval.glo4002.cafe.api.operation.request.CheckInRequest;
import ca.ulaval.glo4002.cafe.api.operation.request.CheckOutRequest;
import ca.ulaval.glo4002.cafe.service.CafeService;
import ca.ulaval.glo4002.cafe.service.customer.CustomerService;
import ca.ulaval.glo4002.cafe.service.customer.parameter.CheckInCustomerParams;
import ca.ulaval.glo4002.cafe.service.customer.parameter.CheckOutCustomerParams;

import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class OperationResource {
    private final CafeService cafeService;
    private final CustomerService customersService;

    public OperationResource(CafeService cafeService, CustomerService customersService) {
        this.cafeService = cafeService;
        this.customersService = customersService;
    }

    @POST
    @Path("/close")
    public Response close() {
        cafeService.closeCafe();
        return Response.ok().build();
    }

    @POST
    @Path("/check-in")
    public Response checkIn(@Valid CheckInRequest checkInRequest) {
        CheckInCustomerParams checkInCustomerParams =
            CheckInCustomerParams.from(checkInRequest.customer_id, checkInRequest.customer_name, checkInRequest.group_name);
        customersService.checkIn(checkInCustomerParams);
        return Response.created(URI.create("/customers/" + checkInCustomerParams.customerId().value())).build();
    }

    @POST
    @Path("/checkout")
    public Response checkOut(@Valid CheckOutRequest checkOutRequest) {
        CheckOutCustomerParams checkOutCustomerParams = CheckOutCustomerParams.from(checkOutRequest.customer_id);
        customersService.checkOut(checkOutCustomerParams);
        return Response.created(URI.create("/customers/" + checkOutCustomerParams.customerId().value() + "/bill")).build();
    }
}
