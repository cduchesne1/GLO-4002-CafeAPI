package ca.ulaval.glo4002.cafe.domain.billing;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.ordering.Order;

public record Bill(Order order, Amount subtotal, Amount taxes, Amount tip, Amount total) {
    public Bill(Order order, Amount subtotal, Amount taxes, Amount tip) {
        this(order, subtotal, taxes, tip, subtotal.add(taxes).add(tip));
    }
}
