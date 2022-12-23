package ca.ulaval.glo4002.cafe.fixture;

import java.util.Optional;

import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.bill.TipRate;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.location.Province;
import ca.ulaval.glo4002.cafe.domain.location.State;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.DefaultStrategy;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.ReservationStrategy;

public class CafeConfigurationFixture {
    private int cubeSize = 4;
    private CafeName name = new CafeName("Les 4-Fées");
    private ReservationStrategy reservationStrategy = new DefaultStrategy();
    private Country country = Country.CA;
    private Optional<Province> province = Optional.of(Province.QC);
    private Optional<State> state = Optional.empty();
    private TipRate groupTipRate = new TipRate(0.05f);

    public CafeConfigurationFixture withCubeSize(int cubeSize) {
        this.cubeSize = cubeSize;
        return this;
    }

    public CafeConfigurationFixture withName(CafeName name) {
        this.name = name;
        return this;
    }

    public CafeConfigurationFixture withReservationStrategy(ReservationStrategy reservationStrategy) {
        this.reservationStrategy = reservationStrategy;
        return this;
    }

    public CafeConfigurationFixture withCountry(Country country) {
        this.country = country;
        return this;
    }

    public CafeConfigurationFixture withProvince(Province province) {
        this.province = Optional.of(province);
        return this;
    }

    public CafeConfigurationFixture withState(State state) {
        this.state = Optional.of(state);
        return this;
    }

    public CafeConfigurationFixture withGroupTipRate(TipRate groupTipRate) {
        this.groupTipRate = groupTipRate;
        return this;
    }

    public CafeConfiguration build() {
        return new CafeConfiguration(cubeSize, name, reservationStrategy, new Location(country, province, state), groupTipRate);
    }
}
