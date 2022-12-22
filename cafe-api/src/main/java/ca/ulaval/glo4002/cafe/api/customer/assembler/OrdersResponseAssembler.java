package ca.ulaval.glo4002.cafe.api.customer.assembler;

import ca.ulaval.glo4002.cafe.api.customer.response.OrdersResponse;
import ca.ulaval.glo4002.cafe.application.customer.payload.OrderPayload;

public class OrdersResponseAssembler {
    public OrdersResponse toOrdersResponse(OrderPayload orderPayload) {
        return new OrdersResponse(orderPayload.coffees().stream().map(coffee -> coffee.coffeeType().toString()).toList());
    }
}
