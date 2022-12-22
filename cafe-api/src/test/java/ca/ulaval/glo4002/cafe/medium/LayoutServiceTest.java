package ca.ulaval.glo4002.cafe.medium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.layout.LayoutService;
import ca.ulaval.glo4002.cafe.application.layout.payload.LayoutPayload;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.util.CafeRepositoryTestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LayoutServiceTest {
    private LayoutService layoutService;
    private Cafe cafe;
    private CafeRepository cafeRepository;

    @BeforeEach
    public void setupLayoutService() {
        cafeRepository = CafeRepositoryTestUtil.createCafeRepositoryWithDefaultCafe();
        layoutService = new LayoutService(cafeRepository);
        cafe = cafeRepository.get();
    }

    @Test
    public void whenGettingLayout_shouldReturnLayoutPayload() {
        LayoutPayload layoutPayload = layoutService.getLayout();

        cafe = cafeRepository.get();
        assertEquals(cafe.getName(), layoutPayload.name());
        assertEquals(cafe.getLayout().getCubes().size(), layoutPayload.cubes().size());
    }
}
