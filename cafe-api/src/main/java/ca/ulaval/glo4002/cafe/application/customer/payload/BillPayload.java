package ca.ulaval.glo4002.cafe.application.customer.payload;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.Amount;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.bill.Bill;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.order.Coffee;

public record BillPayload(List<Coffee> coffees, Amount tip, Amount subtotal, Amount taxes, Amount total) {
    public static BillPayload fromBill(Bill bill) {
        return new BillPayload(List.copyOf(bill.order().items()), bill.tip(), bill.subtotal(), bill.taxes(), bill.total());
    }
}
