package ca.ulaval.glo4002.cafe.application.configuration;

import ca.ulaval.glo4002.cafe.application.configuration.query.UpdateConfigurationQuery;
import ca.ulaval.glo4002.cafe.application.configuration.query.UpdateMenuQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.reservation.ReservationStrategyFactory;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.ReservationStrategy;

public class ConfigurationService {
    private final CafeRepository cafeRepository;
    private final ReservationStrategyFactory reservationStrategyFactory;

    public ConfigurationService(CafeRepository cafeRepository, ReservationStrategyFactory reservationStrategyFactory) {
        this.cafeRepository = cafeRepository;
        this.reservationStrategyFactory = reservationStrategyFactory;
    }

    public void updateConfiguration(UpdateConfigurationQuery updateConfigurationQuery) {
        Cafe cafe = cafeRepository.get();

        ReservationStrategy reservationStrategy = reservationStrategyFactory.createReservationStrategy(updateConfigurationQuery.reservationType());
        CafeConfiguration cafeConfiguration =
            new CafeConfiguration(updateConfigurationQuery.cubeSize(), updateConfigurationQuery.cafeName(), reservationStrategy,
                updateConfigurationQuery.location(), updateConfigurationQuery.groupTipRate());
        cafe.updateConfiguration(cafeConfiguration);

        cafe.close();
        cafeRepository.saveOrUpdate(cafe);
    }

    public void updateMenu(UpdateMenuQuery updateMenuQuery) {
        Cafe cafe = cafeRepository.get();
    }
}
