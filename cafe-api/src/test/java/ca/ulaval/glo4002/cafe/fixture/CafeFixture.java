package ca.ulaval.glo4002.cafe.fixture;

import java.util.List;
import java.util.Optional;

import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.TipRate;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeSize;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.DefaultStrategy;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.ReservationStrategy;

public class CafeFixture {
    private static final ReservationStrategy RESERVATION_STRATEGY = new DefaultStrategy();
    private List<CubeName> cubeNames =
        List.of(new CubeName("Wanda"), new CubeName("Tinker Bell"), new CubeName("Bloom"), new CubeName("Merryweather"));
    private CafeName name = new CafeName("Les 4-Fées");
    private CubeSize cubeSize = new CubeSize(4);
    private TipRate groupTipRate = new TipRate(0);
    private Location location = new Location(Country.None, Optional.empty(), Optional.empty());

    public CafeFixture withName(CafeName name) {
        this.name = name;
        return this;
    }

    public CafeFixture withCubeNames(List<CubeName> cubeNames) {
        this.cubeNames = cubeNames;
        return this;
    }

    public CafeFixture withCubeSize(CubeSize cubeSize) {
        this.cubeSize = cubeSize;
        return this;
    }

    public CafeFixture withLocation(Location location) {
        this.location = location;
        return this;
    }

    public CafeFixture withGroupTipRate(TipRate groupTipRate) {
        this.groupTipRate = groupTipRate;
        return this;
    }

    public Cafe build() {
        CafeConfiguration cafeConfiguration = new CafeConfiguration(cubeSize, name, RESERVATION_STRATEGY, location, groupTipRate);
        return new Cafe(cubeNames, cafeConfiguration);
    }
}
