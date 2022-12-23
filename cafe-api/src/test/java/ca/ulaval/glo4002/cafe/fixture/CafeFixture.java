package ca.ulaval.glo4002.cafe.fixture;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ca.ulaval.glo4002.cafe.domain.Amount;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.PointOfSale;
import ca.ulaval.glo4002.cafe.domain.billing.BillFactory;
import ca.ulaval.glo4002.cafe.domain.billing.TipRate;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Inventory;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;
import ca.ulaval.glo4002.cafe.domain.layout.Layout;
import ca.ulaval.glo4002.cafe.domain.layout.cube.Cube;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.ordering.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.ordering.menu.Coffee;
import ca.ulaval.glo4002.cafe.domain.ordering.menu.Menu;
import ca.ulaval.glo4002.cafe.domain.reservation.BookingRegister;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.DefaultStrategy;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.ReservationStrategy;

public class CafeFixture {
    private static final ReservationStrategy RESERVATION_STRATEGY = new DefaultStrategy();
    private final Menu menu = new Menu(Map.ofEntries(Map.entry(new CoffeeName("Americano"),
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

            Map.of(IngredientType.Espresso, new Quantity(50), IngredientType.Milk, new Quantity(40), IngredientType.Chocolate, new Quantity(10))))));
    private CafeConfiguration cafeConfiguration =
        new CafeConfiguration(4, new CafeName("Les 4-Fées"), RESERVATION_STRATEGY, new Location(Country.None, Optional.empty(), Optional.empty()),
            new TipRate(0));
    private Layout layout = new Layout(cafeConfiguration.cubeSize(),
        List.of(new CubeName("Wanda"), new CubeName("Tinker Bell"), new CubeName("Bloom"), new CubeName("Merryweather")));

    public CafeFixture withName(CafeName name) {
        this.cafeConfiguration =
            new CafeConfiguration(cafeConfiguration.cubeSize(), name, RESERVATION_STRATEGY, cafeConfiguration.location(), cafeConfiguration.groupTipRate());
        return this;
    }

    public CafeFixture withCubeNames(List<CubeName> cubeNames) {
        this.layout = new Layout(cafeConfiguration.cubeSize(), cubeNames);
        return this;
    }

    public CafeFixture withCubeSize(int cubeSize) {
        this.cafeConfiguration =
            new CafeConfiguration(cubeSize, cafeConfiguration.cafeName(), RESERVATION_STRATEGY, cafeConfiguration.location(), cafeConfiguration.groupTipRate());
        this.layout = new Layout(cubeSize, layout.getCubes().stream().map(Cube::getName).toList());
        return this;
    }

    public CafeFixture withLocation(Location location) {
        this.cafeConfiguration =
            new CafeConfiguration(cafeConfiguration.cubeSize(), cafeConfiguration.cafeName(), RESERVATION_STRATEGY, location, cafeConfiguration.groupTipRate());
        return this;
    }

    public CafeFixture withGroupTipRate(TipRate groupTipRate) {
        this.cafeConfiguration =
            new CafeConfiguration(cafeConfiguration.cubeSize(), cafeConfiguration.cafeName(), RESERVATION_STRATEGY, cafeConfiguration.location(), groupTipRate);
        return this;
    }

    public Cafe build() {
        return new Cafe(this.cafeConfiguration, this.menu, this.layout, new BookingRegister(), new PointOfSale(new BillFactory()), new Inventory());
    }
}
