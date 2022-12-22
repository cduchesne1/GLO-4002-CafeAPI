package ca.ulaval.glo4002.cafe.application.configuration;

import ca.ulaval.glo4002.cafe.application.configuration.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;

public class ConfigurationService {
    private final CafeRepository cafeRepository;

    public ConfigurationService(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
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
}
