package ca.ulaval.glo4002.cafe.application;

import ca.ulaval.glo4002.cafe.application.payload.LayoutPayload;
import ca.ulaval.glo4002.cafe.application.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;

public class CafeService {
    private final CafeRepository cafeRepository;

    public CafeService(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    public LayoutPayload getLayout() {
        Cafe cafe = cafeRepository.get();
        return LayoutPayload.fromCafe(cafe);
    }

    public void updateConfiguration(UpdateConfigurationQuery updateConfigurationQuery) {
        Cafe cafe = cafeRepository.get();
        CafeConfiguration cafeConfiguration =
            new CafeConfiguration(updateConfigurationQuery.cubeSize(), updateConfigurationQuery.cafeName(), updateConfigurationQuery.reservationType(),
                updateConfigurationQuery.location(), updateConfigurationQuery.groupTipRate());
        cafe.updateConfiguration(cafeConfiguration);
        cafe.close();
        cafeRepository.saveOrUpdate(cafe);
    }

    public void closeCafe() {
        Cafe cafe = cafeRepository.get();
        cafe.close();
        cafeRepository.saveOrUpdate(cafe);
    }
}
