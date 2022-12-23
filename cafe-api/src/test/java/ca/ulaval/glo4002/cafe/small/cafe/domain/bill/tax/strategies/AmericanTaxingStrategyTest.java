package ca.ulaval.glo4002.cafe.small.cafe.domain.bill.tax.strategies;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.AmericanTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.TaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.location.State;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AmericanTaxingStrategyTest {
    private static final Amount AN_AMOUNT = new Amount(100);
    private static final Location AN_AMERICAN_LOCATION = new Location(Country.US, Optional.empty(), Optional.of(State.NY));

    private static TaxingStrategy taxingStrategy;

    @BeforeAll
    public static void assignStrategy() {
        taxingStrategy = new AmericanTaxingStrategy();
    }

    @Test
    public void whenCalculatingTax_shouldReturnRightTax() {
        assertEquals(AN_AMOUNT.value() * 0.04f, taxingStrategy.calculateTax(AN_AMOUNT, AN_AMERICAN_LOCATION));
    }
}
