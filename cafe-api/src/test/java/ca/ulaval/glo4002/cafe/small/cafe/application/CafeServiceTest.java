package ca.ulaval.glo4002.cafe.small.cafe.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import ca.ulaval.glo4002.cafe.application.CafeService;
import ca.ulaval.glo4002.cafe.application.payload.LayoutPayload;
import ca.ulaval.glo4002.cafe.application.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.fixture.CafeFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CafeServiceTest {
    private static final Cafe A_CAFE = new CafeFixture().build();
    private static final UpdateConfigurationQuery AN_UPDATE_CONFIGURATION_QUERY = new UpdateConfigurationQuery(4, "Les 4-Fées", "Default", "CA", "QC", "", 5);
    private CafeService cafeService;
    private CafeRepository cafeRepository;

    @BeforeEach
    public void createCafeService() {
        cafeRepository = mock(CafeRepository.class);
        cafeService = new CafeService(cafeRepository);
    }

    @Test
    public void whenGettingLayout_shouldGetCafe() {
        when(cafeRepository.get()).thenReturn(A_CAFE);

        cafeService.getLayout();

        verify(cafeRepository).get();
    }

    @Test
    public void whenGettingLayout_shouldReturnLayoutPayload() {
        when(cafeRepository.get()).thenReturn(A_CAFE);
        LayoutPayload expectedLayoutPayload = new LayoutPayload(A_CAFE.getName(), A_CAFE.getLayout().getCubes());

        LayoutPayload actualLayoutPayload = cafeService.getLayout();

        assertEquals(expectedLayoutPayload, actualLayoutPayload);
    }

    @Test
    public void whenClosingCafe_shouldGetCafe() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        cafeService.closeCafe();

        verify(cafeRepository).get();
    }

    @Test
    public void whenClosingCafe_shouldCloseCafe() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        cafeService.closeCafe();

        verify(mockCafe).close();
    }

    @Test
    public void whenClosingCafe_shouldUpdateCafe() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        cafeService.closeCafe();

        verify(cafeRepository).saveOrUpdate(mockCafe);
    }

    @Test
    public void whenUpdatingConfiguration_shouldGetCafe() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        cafeService.updateConfiguration(AN_UPDATE_CONFIGURATION_QUERY);

        verify(cafeRepository).get();
    }

    @Test
    public void whenUpdatingConfiguration_shouldCloseCafe() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        cafeService.updateConfiguration(AN_UPDATE_CONFIGURATION_QUERY);

        verify(mockCafe).close();
    }

    @Test
    public void whenUpdatingConfiguration_shouldUpdateCafeConfiguration() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);
        ArgumentCaptor<CafeConfiguration> argument = ArgumentCaptor.forClass(CafeConfiguration.class);
        CafeConfiguration expectedConfiguration = new CafeConfiguration(AN_UPDATE_CONFIGURATION_QUERY.cubeSize(), AN_UPDATE_CONFIGURATION_QUERY.cafeName(),
            AN_UPDATE_CONFIGURATION_QUERY.reservationType(), AN_UPDATE_CONFIGURATION_QUERY.location(), AN_UPDATE_CONFIGURATION_QUERY.groupTipRate());

        cafeService.updateConfiguration(AN_UPDATE_CONFIGURATION_QUERY);

        verify(mockCafe).updateConfiguration(argument.capture());
        assertEquals(expectedConfiguration, argument.getValue());
    }

    @Test
    public void whenUpdatingConfiguration_shouldUpdateCafe() {
        Cafe mockCafe = mock(Cafe.class);
        when(cafeRepository.get()).thenReturn(mockCafe);

        cafeService.updateConfiguration(AN_UPDATE_CONFIGURATION_QUERY);

        verify(cafeRepository).saveOrUpdate(mockCafe);
    }
}
