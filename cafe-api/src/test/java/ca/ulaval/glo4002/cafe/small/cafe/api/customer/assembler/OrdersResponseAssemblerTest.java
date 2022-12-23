package ca.ulaval.glo4002.cafe.small.cafe.api.customer.assembler;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.customer.assembler.OrdersResponseAssembler;
import ca.ulaval.glo4002.cafe.api.customer.response.OrdersResponse;
import ca.ulaval.glo4002.cafe.application.customer.payload.OrderPayload;
import ca.ulaval.glo4002.cafe.domain.ordering.CoffeeName;
import ca.ulaval.glo4002.cafe.fixture.OrderFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrdersResponseAssemblerTest {
    private static final CoffeeName AN_AMERICANO_COFFEE = new CoffeeName("Americano");
    private static final CoffeeName A_DARK_ROAST_COFFEE = new CoffeeName("Dark Roast");

    private OrdersResponseAssembler ordersResponseAssembler;

    @BeforeEach
    public void createAssembler() {
        ordersResponseAssembler = new OrdersResponseAssembler();
    }

    @Test
    public void whenAssemblingOrdersResponse_shouldReturnOrdersResponseWithItemsInRightOrder() {
        OrderPayload orderPayload = OrderPayload.fromOrder(new OrderFixture().withItems(List.of(AN_AMERICANO_COFFEE, A_DARK_ROAST_COFFEE)).build());

        OrdersResponse actualOrderResponse = ordersResponseAssembler.toOrdersResponse(orderPayload);

        assertEquals(List.of("Americano", "Dark Roast"), actualOrderResponse.orders());
    }
}
