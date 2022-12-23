package ca.ulaval.glo4002.cafe.domain.bill;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.bill.tax.TaxingStrategyFactory;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.TaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.order.Order;

public class BillFactory {
    private final TaxingStrategyFactory taxingStrategyFactory = new TaxingStrategyFactory();

    public Bill createBill(Order order, Location location, TipRate groupTipRate, boolean isInGroup) {
        Amount subtotal = getOrderSubtotal(order);
        Amount taxes = calculateTaxes(subtotal, location);
        Amount tip = isInGroup ? new Amount(subtotal.value() * groupTipRate.value()) : new Amount(0);
        return new Bill(new Order(order.items()), subtotal, taxes, tip);
    }

    private Amount getOrderSubtotal(Order order) {
        return new Amount(order.items().stream().map(coffee -> coffee.price().value()).reduce(0f, Float::sum));
    }

    private Amount calculateTaxes(Amount subtotal, Location location) {
        TaxingStrategy taxingStrategy = taxingStrategyFactory.createTaxingStrategy(location.country());
        return new Amount(taxingStrategy.calculateTax(subtotal, location));
    }
}
