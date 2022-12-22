package ca.ulaval.glo4002.cafe.util;

import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.fixture.CafeFixture;
import ca.ulaval.glo4002.cafe.infrastructure.InMemoryCafeRepository;

public class CafeRepositoryTestUtil {
    public static CafeRepository createCafeRepositoryWithDefaultCafe() {
        CafeRepository cafeRepository = new InMemoryCafeRepository();
        cafeRepository.saveOrUpdate(new CafeFixture().build());
        return cafeRepository;
    }
}
