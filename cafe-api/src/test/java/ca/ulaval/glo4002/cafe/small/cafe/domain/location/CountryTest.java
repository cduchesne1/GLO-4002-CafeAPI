package ca.ulaval.glo4002.cafe.small.cafe.domain.location;

import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.exception.InvalidConfigurationCountryException;
import ca.ulaval.glo4002.cafe.domain.location.Country;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CountryTest {
    private static final String INVALID_COUNTRY = "WWW";

    @Test
    public void givenValidCountry_whenCreatingFromString_shouldCreateInstance() {
        Country createdCountry = Country.fromString("CA");

        assertEquals(Country.CA, createdCountry);
    }

    @Test
    public void givenInvalidCountry_whenCreatingFromString_shouldThrowInvalidConfigurationCountryException() {
        assertThrows(InvalidConfigurationCountryException.class, () -> Country.fromString(INVALID_COUNTRY));
    }
}
