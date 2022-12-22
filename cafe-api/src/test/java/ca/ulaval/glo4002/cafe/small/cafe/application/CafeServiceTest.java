package ca.ulaval.glo4002.cafe.small.cafe.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.CafeService;
import ca.ulaval.glo4002.cafe.application.payload.LayoutPayload;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.fixture.CafeFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CafeServiceTest {
    private static final Cafe A_CAFE = new CafeFixture().build();
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
}
