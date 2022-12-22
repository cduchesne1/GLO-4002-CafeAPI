package ca.ulaval.glo4002.cafe.api.customer.assembler;

import ca.ulaval.glo4002.cafe.api.customer.response.BillResponse;
import ca.ulaval.glo4002.cafe.application.customer.payload.BillPayload;

public class BillResponseAssembler {
    public BillResponse toBillResponse(BillPayload billPayload) {
        return new BillResponse(billPayload.coffees().stream().map(coffee -> coffee.coffeeType().toString()).toList(), billPayload.subtotal().getRoundedValue(),
            billPayload.taxes().getRoundedValue(), billPayload.tip().getRoundedValue(), billPayload.total().getRoundedValue());
    }
}
