package ca.ulaval.glo4002.cafe.fixture;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.bill.Bill;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeType;
import ca.ulaval.glo4002.cafe.domain.order.Order;

public class BillFixture {
    private Order coffeeOrder =
        new Order(List.of(new CoffeeName(CoffeeType.Americano.toString()), new CoffeeName(CoffeeType.Espresso.toString()),
            new CoffeeName(CoffeeType.Latte.toString())));
    private Amount subtotal = new Amount(10.0f);
    private Amount taxes = new Amount(1.0f);
    private Amount tip = new Amount(2.0f);

    public BillFixture withCoffeeOrder(Order coffeeOrder) {
        this.coffeeOrder = coffeeOrder;
        return this;
    }

    public BillFixture withSubtotal(Amount subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public BillFixture withTaxes(Amount taxes) {
        this.taxes = taxes;
        return this;
    }

    public BillFixture withTip(Amount tip) {
        this.tip = tip;
        return this;
    }

    public Bill build() {
        return new Bill(coffeeOrder, subtotal, taxes, tip);
    }
}
