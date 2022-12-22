package ca.ulaval.glo4002.cafe.api.customer.assembler;

import ca.ulaval.glo4002.cafe.api.customer.response.CustomerResponse;
import ca.ulaval.glo4002.cafe.application.customer.payload.CustomerPayload;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupName;

public class CustomerResponseAssembler {
    public CustomerResponse toCustomerResponse(CustomerPayload customerPayload) {
        return new CustomerResponse(customerPayload.name().value(), customerPayload.seatNumber().value(),
            customerPayload.groupName().map(GroupName::value).orElse(null));
    }
}
