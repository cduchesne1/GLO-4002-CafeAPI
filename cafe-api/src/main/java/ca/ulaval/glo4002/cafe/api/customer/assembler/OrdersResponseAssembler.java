package ca.ulaval.glo4002.cafe.api.customer.assembler;

import ca.ulaval.glo4002.cafe.api.customer.response.OrdersResponse;
import ca.ulaval.glo4002.cafe.application.customer.payload.OrderPayload;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeName;

public class OrdersResponseAssembler {
    public OrdersResponse toOrdersResponse(OrderPayload orderPayload) {
        return new OrdersResponse(orderPayload.coffees().stream().map(CoffeeName::value).toList());
    }
}
