package ca.ulaval.glo4002.cafe.small.cafe.domain;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeFactory;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.TipRate;
import ca.ulaval.glo4002.cafe.domain.layout.cube.Cube;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeSize;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.DefaultStrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CafeFactoryTest {
    private static final CafeConfiguration A_CAFE_CONFIGURATION = new CafeConfiguration(new CubeSize(4), new CafeName("Les 4-Fées"), new DefaultStrategy(),
        new Location(Country.None, Optional.empty(), Optional.empty()), new TipRate(0));

    private CafeFactory cafeFactory;

    @BeforeEach
    public void createCafeFactory() {
        cafeFactory = new CafeFactory();
    }

    @Test
    public void whenCreatingCafe_shouldCreateCubesListWithSortedSpecificCubesNames() {
        List<CubeName> cubeNames = List.of(new CubeName("Merryweather"), new CubeName("Tinker Bell"), new CubeName("Wanda"), new CubeName("Bloom"));

        Cafe cafe = cafeFactory.createCafe(cubeNames, A_CAFE_CONFIGURATION);

        assertEquals(cubeNames.stream().sorted().toList(), cafe.getLayout().getCubes().stream().map(Cube::getName).toList());
    }
}
