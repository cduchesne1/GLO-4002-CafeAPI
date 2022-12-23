package ca.ulaval.glo4002.cafe.small.cafe.domain.bill.tax.strategies;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.CanadianTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.TaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.location.Province;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CanadianTaxingStrategyTest {
    private static final Amount AN_AMOUNT = new Amount(100);
    private static final Location A_CANADIAN_LOCATION = new Location(Country.CA, Optional.of(Province.NS), Optional.empty());

    private static TaxingStrategy taxingStrategy;

    @BeforeAll
    public static void assignStrategy() {
        taxingStrategy = new CanadianTaxingStrategy();
    }

    @Test
    public void whenCalculatingTax_shouldReturnRightTax() {
        assertEquals(AN_AMOUNT.value() * 0.15f, taxingStrategy.calculateTax(AN_AMOUNT, A_CANADIAN_LOCATION));
    }
}
