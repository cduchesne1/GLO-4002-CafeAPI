package ca.ulaval.glo4002.cafe.domain.bill.tax;

import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.AmericanTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.CanadianTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.ChileanTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.NoTaxTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.TaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.location.Country;

public class TaxingStrategyFactory {
    public TaxingStrategy createTaxingStrategy(Country country) {
        return switch (country) {
            case None -> new NoTaxTaxingStrategy();
            case CL -> new ChileanTaxingStrategy();
            case CA -> new CanadianTaxingStrategy();
            case US -> new AmericanTaxingStrategy();
        };
    }
}
