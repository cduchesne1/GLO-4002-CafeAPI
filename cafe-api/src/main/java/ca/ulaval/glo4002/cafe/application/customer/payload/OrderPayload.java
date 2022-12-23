package ca.ulaval.glo4002.cafe.application.customer.payload;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.ordering.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.ordering.Order;

public record OrderPayload(List<CoffeeName> coffees) {
    public static OrderPayload fromOrder(Order order) {
        return new OrderPayload(order.items().stream().toList());
    }
}
