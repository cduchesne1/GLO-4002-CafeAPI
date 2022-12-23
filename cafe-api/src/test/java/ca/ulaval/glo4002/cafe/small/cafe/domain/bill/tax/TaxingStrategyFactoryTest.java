package ca.ulaval.glo4002.cafe.small.cafe.domain.bill.tax;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.bill.tax.TaxingStrategyFactory;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.AmericanTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.CanadianTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.ChileanTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.NoTaxTaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.bill.tax.strategies.TaxingStrategy;
import ca.ulaval.glo4002.cafe.domain.location.Country;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaxingStrategyFactoryTest {

    private TaxingStrategyFactory taxingStrategyFactory;

    @BeforeEach
    public void createFactory() {
        taxingStrategyFactory = new TaxingStrategyFactory();
    }

    @Test
    public void givenNoneCountry_whenCreateTaxingStrategy_shouldReturnNoTaxTaxingStrategy() {
        TaxingStrategy taxingStrategy = taxingStrategyFactory.createTaxingStrategy(Country.None);

        assertEquals(NoTaxTaxingStrategy.class, taxingStrategy.getClass());
    }

    @Test
    public void givenChileCountry_whenCreateTaxingStrategy_shouldReturnChileanTaxingStrategy() {
        TaxingStrategy taxingStrategy = taxingStrategyFactory.createTaxingStrategy(Country.CL);

        assertEquals(ChileanTaxingStrategy.class, taxingStrategy.getClass());
    }

    @Test
    public void givenCanadaCountry_whenCreateTaxingStrategy_shouldReturnCanadianTaxingStrategy() {
        TaxingStrategy taxingStrategy = taxingStrategyFactory.createTaxingStrategy(Country.CA);

        assertEquals(CanadianTaxingStrategy.class, taxingStrategy.getClass());
    }

    @Test
    public void givenUnitedStatesCountry_whenCreateTaxingStrategy_shouldReturnAmericanTaxingStrategy() {
        TaxingStrategy taxingStrategy = taxingStrategyFactory.createTaxingStrategy(Country.US);

        assertEquals(AmericanTaxingStrategy.class, taxingStrategy.getClass());
    }
}
