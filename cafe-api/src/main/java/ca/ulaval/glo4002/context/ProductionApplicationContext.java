package ca.ulaval.glo4002.context;

import java.util.List;
import java.util.Map;
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
import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeFactory;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.billing.BillFactory;
import ca.ulaval.glo4002.cafe.domain.billing.TipRate;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;
import ca.ulaval.glo4002.cafe.domain.layout.LayoutFactory;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerFactory;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.ordering.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.ordering.menu.Coffee;
import ca.ulaval.glo4002.cafe.domain.ordering.menu.MenuFactory;
import ca.ulaval.glo4002.cafe.domain.reservation.ReservationFactory;
import ca.ulaval.glo4002.cafe.domain.reservation.ReservationStrategyFactory;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.DefaultStrategy;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.ReservationStrategy;
import ca.ulaval.glo4002.cafe.infrastructure.InMemoryCafeRepository;

public class ProductionApplicationContext implements ApplicationContext {
    private static final int PORT = 8181;

    private static final CafeName CAFE_NAME = new CafeName("Les 4-Fées");
    private static final int CUBE_SIZE = 4;
    private static final List<CubeName> CUBE_NAMES =
        List.of(new CubeName("Wanda"), new CubeName("Tinker Bell"), new CubeName("Bloom"), new CubeName("Merryweather"));
    private static final ReservationStrategy RESERVATION_STRATEGY = new DefaultStrategy();
    private static final TipRate GROUP_TIP_RATE = new TipRate(0);
    private static final Location LOCATION = new Location(Country.None, Optional.empty(), Optional.empty());

    private static final Map<CoffeeName, Coffee> DEFAULT_COFFEES = Map.ofEntries(Map.entry(new CoffeeName("Americano"),
            new Coffee(new CoffeeName("Americano"), new Amount(2.25f),
                Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Water, new Quantity(50)))),

        Map.entry(new CoffeeName("Dark Roast"), new Coffee(new CoffeeName("Dark Roast"), new Amount(2.10f),
            Map.of(IngredientType.Espresso, new Quantity(40), IngredientType.Water, new Quantity(40), IngredientType.Chocolate, new Quantity(10),
                IngredientType.Milk, new Quantity(10)))),

        Map.entry(new CoffeeName("Cappuccino"), new Coffee(new CoffeeName("Cappuccino"), new Amount(3.29f),
            Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Water, new Quantity(40), IngredientType.Milk, new Quantity(10)))),

        Map.entry(new CoffeeName("Espresso"), new Coffee(new CoffeeName("Espresso"), new Amount(2.95f), Map.of(IngredientType.Espresso, new Quantity(60)))),

        Map.entry(new CoffeeName("Flat White"), new Coffee(new CoffeeName("Flat White"), new Amount(3.75f),
            Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Milk, new Quantity(50)))),

        Map.entry(new CoffeeName("Latte"),
            new Coffee(new CoffeeName("Latte"), new Amount(2.95f), Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Milk, new Quantity(50)))),

        Map.entry(new CoffeeName("Macchiato"), new Coffee(new CoffeeName("Macchiato"), new Amount(4.75f),
            Map.of(IngredientType.Espresso, new Quantity(80), IngredientType.Milk, new Quantity(20)))),

        Map.entry(new CoffeeName("Mocha"), new Coffee(new CoffeeName("Mocha"), new Amount(4.15f),
            Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Milk, new Quantity(40), IngredientType.Chocolate, new Quantity(10)))));

    public ResourceConfig initializeResourceConfig() {
        CafeRepository cafeRepository = new InMemoryCafeRepository();

        ReservationService groupService = new ReservationService(cafeRepository, new ReservationFactory());
        CustomerService customersService = new CustomerService(cafeRepository, new CustomerFactory(), new BillFactory());
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
        Cafe cafe = new CafeFactory(new LayoutFactory(), new MenuFactory(), new BillFactory()).createCafe(CUBE_NAMES, cafeConfiguration, DEFAULT_COFFEES);
        cafeRepository.saveOrUpdate(cafe);
    }

    public int getPort() {
        return PORT;
    }
}
