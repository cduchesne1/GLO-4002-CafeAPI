package ca.ulaval.glo4002.cafe.application.customer.payload;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.order.Coffee;
import ca.ulaval.glo4002.cafe.domain.order.Order;

public record OrderPayload(List<Coffee> coffees) {
    public static OrderPayload fromOrder(Order order) {
        return new OrderPayload(order.items().stream().toList());
    }
}
