package ca.ulaval.glo4002.cafe.domain.billing.tax.strategies;

import java.util.Map;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.location.State;

public class AmericanTaxingStrategy implements TaxingStrategy {
    private static final float FEDERAL_TAX_RATE = 0;

    private static final Map<State, Float> STATES_TAX_RATES =
        Map.ofEntries(Map.entry(State.AL, 0.04f), Map.entry(State.AZ, 0.056f), Map.entry(State.CA, 0.0725f), Map.entry(State.FL, 0.06f),
            Map.entry(State.ME, 0.055f), Map.entry(State.NY, 0.04f), Map.entry(State.TX, 0.0625f));

    @Override
    public float calculateTax(Amount amount, Location location) {
        float combinedTaxRate = FEDERAL_TAX_RATE + STATES_TAX_RATES.get(location.state().get());
        return amount.value() * combinedTaxRate;
    }
}
