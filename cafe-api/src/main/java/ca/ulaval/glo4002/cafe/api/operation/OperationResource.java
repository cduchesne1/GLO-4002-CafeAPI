package ca.ulaval.glo4002.cafe.api.operation;

import java.net.URI;

import ca.ulaval.glo4002.cafe.api.operation.request.CheckInRequest;
import ca.ulaval.glo4002.cafe.api.operation.request.CheckOutRequest;
import ca.ulaval.glo4002.cafe.application.CafeService;
import ca.ulaval.glo4002.cafe.application.customer.CustomerService;
import ca.ulaval.glo4002.cafe.application.customer.query.CheckInCustomerQuery;
import ca.ulaval.glo4002.cafe.application.customer.query.CheckOutCustomerQuery;

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
        CheckInCustomerQuery checkInCustomerQuery =
            CheckInCustomerQuery.from(checkInRequest.customer_id, checkInRequest.customer_name, checkInRequest.group_name);
        customersService.checkIn(checkInCustomerQuery);
        return Response.created(URI.create("/customers/" + checkInCustomerQuery.customerId().value())).build();
    }

    @POST
    @Path("/checkout")
    public Response checkOut(@Valid CheckOutRequest checkOutRequest) {
        CheckOutCustomerQuery checkOutCustomerQuery = CheckOutCustomerQuery.from(checkOutRequest.customer_id);
        customersService.checkOut(checkOutCustomerQuery);
        return Response.created(URI.create("/customers/" + checkOutCustomerQuery.customerId().value() + "/bill")).build();
    }
}
