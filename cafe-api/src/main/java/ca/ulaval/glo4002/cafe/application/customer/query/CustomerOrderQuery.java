package ca.ulaval.glo4002.cafe.application.customer.query;

import java.util.List;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerId;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.order.Order;

public record CustomerOrderQuery(CustomerId customerId, Order order) {
    public CustomerOrderQuery(String customerId, List<String> orders) {
        this(new CustomerId(customerId), new Order(orders.stream().map(CoffeeName::new).collect(Collectors.toList())));
    }

    public static CustomerOrderQuery from(String customerId, List<String> orders) {
        return new CustomerOrderQuery(customerId, orders);
    }
}
