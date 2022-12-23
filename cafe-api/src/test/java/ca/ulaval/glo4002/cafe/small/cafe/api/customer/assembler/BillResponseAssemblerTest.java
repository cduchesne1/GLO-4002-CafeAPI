package ca.ulaval.glo4002.cafe.small.cafe.api.customer.assembler;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.customer.assembler.BillResponseAssembler;
import ca.ulaval.glo4002.cafe.api.customer.response.BillResponse;
import ca.ulaval.glo4002.cafe.application.customer.payload.BillPayload;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.Amount;
import ca.ulaval.glo4002.cafe.domain.order.Coffee;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeType;
import ca.ulaval.glo4002.cafe.domain.order.Order;
import ca.ulaval.glo4002.cafe.fixture.BillFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BillResponseAssemblerTest {
    private static final Order A_COFFEE_ORDER =
        new Order(List.of(new Coffee(CoffeeType.Espresso), new Coffee(CoffeeType.Espresso), new Coffee(CoffeeType.Latte), new Coffee(CoffeeType.Americano)));

    private BillResponseAssembler billResponseAssembler;

    @BeforeEach
    public void createAssembler() {
        billResponseAssembler = new BillResponseAssembler();
    }

    @Test
    public void givenBillPayload_whenAssemblingBillResponse_shouldAssembleBillResponseWithCoffeeTypeListInSameOrder() {
        BillPayload billPayload = BillPayload.fromBill(new BillFixture().withCoffeeOrder(A_COFFEE_ORDER).build());

        BillResponse actualBillResponse = billResponseAssembler.toBillResponse(billPayload);

        assertEquals(actualBillResponse.orders(), A_COFFEE_ORDER.items().stream().map(coffee -> coffee.coffeeType().toString()).toList());
    }

    @Test
    public void givenAmountWithMoreThanTwoDecimal_whenAssemblingBillResponse_shouldAssembleBillAmountsRoundedUp() {
        Amount anAmountWithMoreThanTwoDecimal = new Amount(4.91001f);
        BillPayload billPayload = BillPayload.fromBill(new BillFixture().withSubtotal(anAmountWithMoreThanTwoDecimal).build());

        BillResponse actualBillResponse = billResponseAssembler.toBillResponse(billPayload);

        assertEquals(4.92f, actualBillResponse.subtotal());
    }
}
