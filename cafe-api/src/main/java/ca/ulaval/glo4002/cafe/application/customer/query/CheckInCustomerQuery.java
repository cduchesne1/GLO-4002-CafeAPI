package ca.ulaval.glo4002.cafe.application.customer.query;

import java.util.Optional;

import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerId;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerName;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupName;

public record CheckInCustomerQuery(CustomerId customerId, CustomerName customerName, Optional<GroupName> groupName) {
    public CheckInCustomerQuery(String customerId, String customerName, String groupName) {
        this(new CustomerId(customerId), new CustomerName(customerName), Optional.ofNullable(groupName != null ? new GroupName(groupName) : null));
    }

    public static CheckInCustomerQuery from(String customerId, String customerName, String groupName) {
        return new CheckInCustomerQuery(customerId, customerName, groupName);
    }
}
