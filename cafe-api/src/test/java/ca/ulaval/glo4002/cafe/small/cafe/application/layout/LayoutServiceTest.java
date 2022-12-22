package ca.ulaval.glo4002.cafe.small.cafe.application.layout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.layout.LayoutService;
import ca.ulaval.glo4002.cafe.application.layout.payload.LayoutPayload;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.fixture.CafeFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LayoutServiceTest {
    private static final Cafe A_CAFE = new CafeFixture().build();
    private LayoutService layoutService;
    private CafeRepository cafeRepository;

    @BeforeEach
    public void setupLayoutService() {
        cafeRepository = mock(CafeRepository.class);
        layoutService = new LayoutService(cafeRepository);
    }

    @Test
    public void whenGettingLayout_shouldGetCafe() {
        when(cafeRepository.get()).thenReturn(A_CAFE);

        layoutService.getLayout();

        verify(cafeRepository).get();
    }

    @Test
    public void whenGettingLayout_shouldReturnLayoutPayload() {
        when(cafeRepository.get()).thenReturn(A_CAFE);
        LayoutPayload expectedLayoutPayload = new LayoutPayload(A_CAFE.getName(), A_CAFE.getLayout().getCubes());

        LayoutPayload actualLayoutPayload = layoutService.getLayout();

        assertEquals(expectedLayoutPayload, actualLayoutPayload);
    }
}
