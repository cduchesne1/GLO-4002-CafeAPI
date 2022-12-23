package ca.ulaval.glo4002.cafe.application.customer.payload;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.bill.Bill;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeName;

public record BillPayload(List<CoffeeName> coffees, Amount tip, Amount subtotal, Amount taxes, Amount total) {
    public static BillPayload fromBill(Bill bill) {
        return new BillPayload(List.copyOf(bill.order().items()), bill.tip(), bill.subtotal(), bill.taxes(), bill.total());
    }
}
