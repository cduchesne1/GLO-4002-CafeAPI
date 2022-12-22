package ca.ulaval.glo4002.cafe.small.cafe.domain.location;

import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.exception.InvalidConfigurationCountryException;
import ca.ulaval.glo4002.cafe.domain.location.Province;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProvinceTest {
    private static final String INVALID_PROVINCE = "WWW";

    @Test
    public void givenValidProvince_whenCreatingFromString_shouldCreateInstance() {
        Province createdProvince = Province.fromString("QC");

        assertEquals(Province.QC, createdProvince);
    }

    @Test
    public void givenInvalidProvince_whenCreatingFromString_shouldThrowInvalidConfigurationCountryException() {
        assertThrows(InvalidConfigurationCountryException.class, () -> Province.fromString(INVALID_PROVINCE));
    }
}
