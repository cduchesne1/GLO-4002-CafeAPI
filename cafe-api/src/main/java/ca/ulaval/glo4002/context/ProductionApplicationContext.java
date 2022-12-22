package ca.ulaval.glo4002.context;

import java.util.List;
import java.util.Optional;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import ca.ulaval.glo4002.cafe.api.configuration.ConfigurationResource;
import ca.ulaval.glo4002.cafe.api.customer.CustomerResource;
import ca.ulaval.glo4002.cafe.api.exception.mapper.CafeExceptionMapper;
import ca.ulaval.glo4002.cafe.api.exception.mapper.CatchallExceptionMapper;
import ca.ulaval.glo4002.cafe.api.exception.mapper.ConstraintViolationExceptionMapper;
import ca.ulaval.glo4002.cafe.api.inventory.InventoryResource;
import ca.ulaval.glo4002.cafe.api.layout.LayoutResource;
import ca.ulaval.glo4002.cafe.api.operation.OperationResource;
import ca.ulaval.glo4002.cafe.api.reservation.ReservationResource;
import ca.ulaval.glo4002.cafe.application.configuration.ConfigurationService;
import ca.ulaval.glo4002.cafe.application.customer.CustomerService;
import ca.ulaval.glo4002.cafe.application.inventory.InventoryService;
import ca.ulaval.glo4002.cafe.application.layout.LayoutService;
import ca.ulaval.glo4002.cafe.application.operation.OperationService;
import ca.ulaval.glo4002.cafe.application.reservation.ReservationService;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeFactory;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.TipRate;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeSize;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerFactory;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.reservation.ReservationFactory;
import ca.ulaval.glo4002.cafe.domain.reservation.ReservationStrategyFactory;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.DefaultStrategy;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.ReservationStrategy;
import ca.ulaval.glo4002.cafe.infrastructure.InMemoryCafeRepository;

public class ProductionApplicationContext implements ApplicationContext {
    private static final int PORT = 8181;

    private static final CafeName CAFE_NAME = new CafeName("Les 4-Fées");
    private static final CubeSize CUBE_SIZE = new CubeSize(4);
    private static final List<CubeName> CUBE_NAMES =
        List.of(new CubeName("Wanda"), new CubeName("Tinker Bell"), new CubeName("Bloom"), new CubeName("Merryweather"));
    private static final ReservationStrategy RESERVATION_STRATEGY = new DefaultStrategy();
    private static final TipRate GROUP_TIP_RATE = new TipRate(0);
    private static final Location LOCATION = new Location(Country.None, Optional.empty(), Optional.empty());

    public ResourceConfig initializeResourceConfig() {
        CafeRepository cafeRepository = new InMemoryCafeRepository();

        ReservationService groupService = new ReservationService(cafeRepository, new ReservationFactory());
        CustomerService customersService = new CustomerService(cafeRepository, new CustomerFactory());
        LayoutService layoutService = new LayoutService(cafeRepository);
        InventoryService inventoryService = new InventoryService(cafeRepository);
        ConfigurationService configurationService = new ConfigurationService(cafeRepository, new ReservationStrategyFactory());
        OperationService operationService = new OperationService(cafeRepository);

        initializeCafe(cafeRepository);

        return new ResourceConfig().packages("ca.ulaval.glo4002.cafe").property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
            .register(new ConfigurationResource(configurationService)).register(new LayoutResource(layoutService))
            .register(new OperationResource(operationService, customersService)).register(new CustomerResource(customersService))
            .register(new InventoryResource(inventoryService)).register(new ReservationResource(groupService)).register(new CafeExceptionMapper())
            .register(new CatchallExceptionMapper()).register(new ConstraintViolationExceptionMapper());
    }

    private void initializeCafe(CafeRepository cafeRepository) {
        CafeConfiguration cafeConfiguration = new CafeConfiguration(CUBE_SIZE, CAFE_NAME, RESERVATION_STRATEGY, LOCATION, GROUP_TIP_RATE);
        Cafe cafe = new CafeFactory().createCafe(CUBE_NAMES, cafeConfiguration);
        cafeRepository.saveOrUpdate(cafe);
    }

    public int getPort() {
        return PORT;
    }
}
