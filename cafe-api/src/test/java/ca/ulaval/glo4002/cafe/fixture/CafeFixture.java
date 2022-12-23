package ca.ulaval.glo4002.cafe.fixture;

import java.util.List;
import java.util.Optional;

import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.bill.BillFactory;
import ca.ulaval.glo4002.cafe.domain.bill.TipRate;
import ca.ulaval.glo4002.cafe.domain.inventory.Inventory;
import ca.ulaval.glo4002.cafe.domain.layout.Layout;
import ca.ulaval.glo4002.cafe.domain.layout.cube.Cube;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.DefaultStrategy;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.ReservationStrategy;

public class CafeFixture {
    private static final ReservationStrategy RESERVATION_STRATEGY = new DefaultStrategy();
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
        return new Cafe(this.cafeConfiguration, this.layout, new BillFactory(), new Inventory());
    }
}
