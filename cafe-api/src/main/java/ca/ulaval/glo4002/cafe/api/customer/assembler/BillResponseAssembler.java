package ca.ulaval.glo4002.cafe.api.customer.assembler;

import ca.ulaval.glo4002.cafe.api.customer.response.BillResponse;
import ca.ulaval.glo4002.cafe.application.customer.payload.BillPayload;
import ca.ulaval.glo4002.cafe.domain.ordering.CoffeeName;

public class BillResponseAssembler {
    public BillResponse toBillResponse(BillPayload billPayload) {
        return new BillResponse(billPayload.coffees().stream().map(CoffeeName::value).toList(), billPayload.subtotal().getRoundedValue(),
            billPayload.taxes().getRoundedValue(), billPayload.tip().getRoundedValue(), billPayload.total().getRoundedValue());
    }
}
