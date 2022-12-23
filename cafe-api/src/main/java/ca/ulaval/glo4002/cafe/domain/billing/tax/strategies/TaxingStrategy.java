package ca.ulaval.glo4002.cafe.domain.billing.tax.strategies;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.location.Location;

public interface TaxingStrategy {
    float calculateTax(Amount amount, Location location);
}
