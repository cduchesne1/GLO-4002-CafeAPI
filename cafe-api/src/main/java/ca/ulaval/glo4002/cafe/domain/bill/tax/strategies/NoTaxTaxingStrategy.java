package ca.ulaval.glo4002.cafe.domain.bill.tax.strategies;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.location.Location;

public class NoTaxTaxingStrategy implements TaxingStrategy {
    @Override
    public float calculateTax(Amount amount, Location location) {
        return 0;
    }
}
