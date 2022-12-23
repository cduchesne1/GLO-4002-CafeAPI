package ca.ulaval.glo4002.cafe.domain.billing.tax.strategies;

import java.util.Map;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.location.Province;

public class CanadianTaxingStrategy implements TaxingStrategy {
    private static final float FEDERAL_TAX_RATE = 0.05f;
    private static final Map<Province, Float> PROVINCES_TAX_RATES =
        Map.ofEntries(Map.entry(Province.AB, 0.0f), Map.entry(Province.BC, 0.07f), Map.entry(Province.MB, 0.07f), Map.entry(Province.NB, 0.10f),
            Map.entry(Province.NL, 0.10f), Map.entry(Province.NT, 0.0f), Map.entry(Province.NS, 0.10f), Map.entry(Province.NU, 0.0f),
            Map.entry(Province.ON, 0.08f), Map.entry(Province.PE, 0.10f), Map.entry(Province.QC, 0.09975f), Map.entry(Province.SK, 0.06f),
            Map.entry(Province.YT, 0.0f));

    @Override
    public float calculateTax(Amount amount, Location location) {
        float combinedTaxRate = FEDERAL_TAX_RATE + PROVINCES_TAX_RATES.get(location.province().get());
        return amount.value() * combinedTaxRate;
    }
}
