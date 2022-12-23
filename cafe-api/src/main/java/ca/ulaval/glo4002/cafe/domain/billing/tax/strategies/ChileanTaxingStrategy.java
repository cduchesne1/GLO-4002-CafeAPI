package ca.ulaval.glo4002.cafe.domain.billing.tax.strategies;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.location.Location;

public class ChileanTaxingStrategy implements TaxingStrategy {
    private static final float TAX_RATE = 0.19f;

    @Override
    public float calculateTax(Amount amount, Location location) {
        return amount.value() * TAX_RATE;
    }
}
