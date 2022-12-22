package ca.ulaval.glo4002.cafe.medium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.CafeService;
import ca.ulaval.glo4002.cafe.application.payload.LayoutPayload;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.util.CafeRepositoryTestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CafeServiceTest {
    private CafeService cafeService;
    private Cafe cafe;
    private CafeRepository cafeRepository;

    @BeforeEach
    public void instanciateAttributes() {
        cafeRepository = CafeRepositoryTestUtil.createCafeRepositoryWithDefaultCafe();
        cafeService = new CafeService(cafeRepository);
        cafe = cafeRepository.get();
    }

    @Test
    public void whenGettingLayout_shouldReturnLayoutPayload() {
        LayoutPayload layoutPayload = cafeService.getLayout();

        cafe = cafeRepository.get();
        assertEquals(cafe.getName(), layoutPayload.name());
        assertEquals(cafe.getLayout().getCubes().size(), layoutPayload.cubes().size());
    }
}
