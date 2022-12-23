package ca.ulaval.glo4002.cafe.small.cafe.domain.billing.tax.strategies;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.billing.tax.strategies.NoTaxTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.billing.tax.strategies.TaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NoTaxTaxingStrategyTest {
    private static final Amount ANY_AMOUNT = new Amount(100);
    private static final Location ANY_LOCATION = new Location(Country.None, Optional.empty(), Optional.empty());

    private static TaxingStrategy taxingStrategy;

    @BeforeAll
    public static void assignStrategy() {
        taxingStrategy = new NoTaxTaxingStrategy();
    }

    @Test
    public void givenAnyAmountAndAnyLocation_whenCalculatingTax_shouldReturnZero() {
        assertEquals(0, taxingStrategy.calculateTax(ANY_AMOUNT, ANY_LOCATION));
    }
}
