package ca.ulaval.glo4002.cafe.small.cafe.domain.location;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.bill.Tax;
import ca.ulaval.glo4002.cafe.domain.exception.InvalidConfigurationCountryException;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.location.Province;
import ca.ulaval.glo4002.cafe.domain.location.State;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationTest {
    private static final String INVALID_COUNTRY = "WWW";
    private static final String INVALID_PROVINCE = "BOB";
    private static final String INVALID_STATE = "EOE";
    private static final String A_VALID_PROVINCE = "QC";
    private static final String A_VALID_STATE = "AL";

    @Test
    public void givenLocationWithCountryAndProvince_whenGettingTaxPercentage_shouldReturnRightTaxPercentage() {
        Location location = new Location(Country.CA, Optional.of(Province.QC), Optional.empty());
        Tax expectedTax = Country.CA.getTax().add(Province.QC.getTax());

        Tax taxReceived = location.getTaxPercentage();

        assertEquals(expectedTax, taxReceived);
    }

    @Test
    public void givenLocationWithCountryAndState_whenGettingTaxPercentage_shouldReturnRightTaxPercentage() {
        Location location = new Location(Country.US, Optional.empty(), Optional.of(State.CA));
        Tax expectedTax = Country.US.getTax().add(State.CA.getTax());

        Tax taxReceived = location.getTaxPercentage();

        assertEquals(expectedTax, taxReceived);
    }

    @Test
    public void givenLocationWithOnlyCountry_whenGettingTaxPercentage_shouldReturnRightTaxPercentage() {
        Location location = new Location(Country.CL, Optional.empty(), Optional.empty());
        Tax expectedTax = Country.CL.getTax();

        Tax taxReceived = location.getTaxPercentage();

        assertEquals(expectedTax, taxReceived);
    }

    @Test
    public void givenLocationWithNoneCountry_whenGettingTaxPercentage_shouldReturnZero() {
        Location location = new Location(Country.None, Optional.empty(), Optional.empty());
        Tax expectedTax = Country.None.getTax();

        Tax taxReceived = location.getTaxPercentage();

        assertEquals(expectedTax, taxReceived);
    }

    @Test
    public void givenAnInvalidCountry_whenCreatingLocationFromDetails_shouldThrowInvalidConfigurationCountryException() {
        assertThrows(InvalidConfigurationCountryException.class,
            () -> Location.fromDetails(INVALID_COUNTRY, A_VALID_PROVINCE, A_VALID_STATE));
    }

    @Test
    public void givenCACountryAndInvalidProvince_whenCreatingLocationFromDetails_shouldThrowInvalidConfigurationCountryException() {
        assertThrows(InvalidConfigurationCountryException.class,
            () -> Location.fromDetails("CA", INVALID_PROVINCE, A_VALID_STATE));
    }

    @Test
    public void givenUSCountryAndInvalidState_whenCreatingLocationFromDetails_shouldThrowInvalidConfigurationCountryException() {
        assertThrows(InvalidConfigurationCountryException.class,
            () -> Location.fromDetails("US", A_VALID_PROVINCE, INVALID_STATE));
    }

    @Test
    public void givenCACountryAndInvalidState_whenCreatingLocationFromDetails_shouldNotThrowInvalidConfigurationRequestException() {
        assertDoesNotThrow(
            () -> Location.fromDetails("CA", A_VALID_PROVINCE, INVALID_STATE));
    }

    @Test
    public void givenUSCountryAndInvalidProvince_whenCreatingLocationFromDetails_shouldNotThrowInvalidConfigurationRequestException() {
        assertDoesNotThrow(
            () -> Location.fromDetails("US", INVALID_PROVINCE, A_VALID_STATE));
    }

    @Test
    public void givenCountryWithProvinceOnly_whenCreatingLocationFromDetails_shouldIgnoreState() {
        Location location = Location.fromDetails("CA", A_VALID_PROVINCE, A_VALID_STATE);

        assertTrue(location.state().isEmpty());
    }

    @Test
    public void givenCountryWithStateOnly_whenCreatingLocationFromDetails_shouldIgnoreProvince() {
        Location location = Location.fromDetails("US", A_VALID_PROVINCE, A_VALID_STATE);

        assertTrue(location.province().isEmpty());
    }

    @Test
    public void givenCountryWithNoProvinceAndState_whenCreatingLocationFromDetails_shouldIgnoreProvinceAndState() {
        Location location = Location.fromDetails("None", A_VALID_PROVINCE, A_VALID_STATE);

        assertTrue(location.province().isEmpty());
        assertTrue(location.state().isEmpty());
    }
}
