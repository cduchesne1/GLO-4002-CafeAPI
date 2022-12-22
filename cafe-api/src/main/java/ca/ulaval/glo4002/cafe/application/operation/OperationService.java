package ca.ulaval.glo4002.cafe.application.operation;

import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;

public class OperationService {
    private final CafeRepository cafeRepository;

    public OperationService(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    public void closeCafe() {
        Cafe cafe = cafeRepository.get();
        cafe.close();
        cafeRepository.saveOrUpdate(cafe);
    }
}
