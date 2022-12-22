package ca.ulaval.glo4002.cafe.application.layout;

import ca.ulaval.glo4002.cafe.application.layout.payload.LayoutPayload;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;

public class LayoutService {
    private final CafeRepository cafeRepository;

    public LayoutService(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    public LayoutPayload getLayout() {
        Cafe cafe = cafeRepository.get();
        return LayoutPayload.fromCafe(cafe);
    }
}
