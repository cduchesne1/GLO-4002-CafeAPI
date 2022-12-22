package ca.ulaval.glo4002.cafe.application.customer.query;

import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerId;

public record CheckOutCustomerQuery(CustomerId customerId) {
    public CheckOutCustomerQuery(String customerId) {
        this(new CustomerId(customerId));
    }

    public static CheckOutCustomerQuery from(String customerId) {
        return new CheckOutCustomerQuery(customerId);
    }
}
