package ca.ulaval.glo4002.cafe.small.cafe.domain.billing.tax.strategies;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.billing.tax.strategies.ChileanTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.billing.tax.strategies.TaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChileanTaxingStrategyTest {
    private static final Amount AN_AMOUNT = new Amount(100);
    private static final Location A_CHILEAN_LOCATION = new Location(Country.CL, Optional.empty(), Optional.empty());

    private static TaxingStrategy taxingStrategy;

    @BeforeAll
    public static void assignStrategy() {
        taxingStrategy = new ChileanTaxingStrategy();
    }

    @Test
    public void whenCalculatingTax_shouldReturnRightTax() {
        float expectedTax = 19;
        assertEquals(expectedTax, taxingStrategy.calculateTax(AN_AMOUNT, A_CHILEAN_LOCATION));
    }
}
