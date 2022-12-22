package ca.ulaval.glo4002.cafe.small.cafe.application.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import ca.ulaval.glo4002.cafe.application.configuration.ConfigurationService;
import ca.ulaval.glo4002.cafe.application.configuration.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfigurationServiceTest {
    private static final UpdateConfigurationQuery AN_UPDATE_CONFIGURATION_QUERY = new UpdateConfigurationQuery(4, "Les 4-Fées", "Default", "CA", "QC", "", 5);

    private ConfigurationService configurationService;
    private CafeRepository cafeRepository;

    @BeforeEach
    public void setupConfigurationService() {
        cafeRepository = mock(CafeRepository.class);
        configurationService = new ConfigurationService(cafeRepository);
    }

    @Test
    public void whenUpdatingConfiguration_shouldGetCafe() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        configurationService.updateConfiguration(AN_UPDATE_CONFIGURATION_QUERY);

        verify(cafeRepository).get();
    }

    @Test
    public void whenUpdatingConfiguration_shouldCloseCafe() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        configurationService.updateConfiguration(AN_UPDATE_CONFIGURATION_QUERY);

        verify(mockCafe).close();
    }

    @Test
    public void whenUpdatingConfiguration_shouldUpdateCafeConfiguration() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);
        ArgumentCaptor<CafeConfiguration> argument = ArgumentCaptor.forClass(CafeConfiguration.class);
        CafeConfiguration expectedConfiguration = new CafeConfiguration(AN_UPDATE_CONFIGURATION_QUERY.cubeSize(), AN_UPDATE_CONFIGURATION_QUERY.cafeName(),
            AN_UPDATE_CONFIGURATION_QUERY.reservationType(), AN_UPDATE_CONFIGURATION_QUERY.location(), AN_UPDATE_CONFIGURATION_QUERY.groupTipRate());

        configurationService.updateConfiguration(AN_UPDATE_CONFIGURATION_QUERY);

        verify(mockCafe).updateConfiguration(argument.capture());
        assertEquals(expectedConfiguration, argument.getValue());
    }

    @Test
    public void whenUpdatingConfiguration_shouldUpdateCafe() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        configurationService.updateConfiguration(AN_UPDATE_CONFIGURATION_QUERY);

        verify(cafeRepository).saveOrUpdate(mockCafe);
    }
}
