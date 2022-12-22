package ca.ulaval.glo4002.cafe.medium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.configuration.ConfigurationService;
import ca.ulaval.glo4002.cafe.application.configuration.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.reservation.ReservationStrategyFactory;
import ca.ulaval.glo4002.cafe.util.CafeRepositoryTestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationServiceTest {
    private static final CafeName NEW_CAFE_NAME = new CafeName("Les 4-Ogres");
    private static final UpdateConfigurationQuery UPDATE_CONFIGURATION_QUERY =
        new UpdateConfigurationQuery(5, NEW_CAFE_NAME.value(), "Default", "CA", "QC", "", 5);

    private ConfigurationService configurationService;
    private CafeRepository cafeRepository;

    @BeforeEach
    public void setupService() {
        cafeRepository = CafeRepositoryTestUtil.createCafeRepositoryWithDefaultCafe();
        configurationService = new ConfigurationService(cafeRepository, new ReservationStrategyFactory());
    }

    @Test
    public void whenUpdatingConfiguration_shouldUpdateConfiguration() {
        configurationService.updateConfiguration(UPDATE_CONFIGURATION_QUERY);

        Cafe cafe = cafeRepository.get();
        assertEquals(NEW_CAFE_NAME, cafe.getName());
    }
}
