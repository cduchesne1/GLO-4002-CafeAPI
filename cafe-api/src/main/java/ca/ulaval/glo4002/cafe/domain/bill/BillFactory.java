package ca.ulaval.glo4002.cafe.domain.bill;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.bill.tax.TaxingStrategyFactory;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.TaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.order.Order;

public class BillFactory {
    private final TaxingStrategyFactory taxingStrategyFactory = new TaxingStrategyFactory();

    public Bill createBill(Order order, List<Amount> itemsPrices, Location location, TipRate groupTipRate, boolean isInGroup) {
        Amount subtotal = getOrderSubtotal(itemsPrices);
        Amount taxes = calculateTaxes(subtotal, location);
        Amount tip = isInGroup ? new Amount(subtotal.value() * groupTipRate.value()) : new Amount(0);
        return new Bill(new Order(order.items()), subtotal, taxes, tip);
    }

    private Amount getOrderSubtotal(List<Amount> itemsPrices) {
        return new Amount(itemsPrices.stream().map(Amount::value).reduce(0f, Float::sum));
    }

    private Amount calculateTaxes(Amount subtotal, Location location) {
        TaxingStrategy taxingStrategy = taxingStrategyFactory.createTaxingStrategy(location.country());
        return new Amount(taxingStrategy.calculateTax(subtotal, location));
    }
}
