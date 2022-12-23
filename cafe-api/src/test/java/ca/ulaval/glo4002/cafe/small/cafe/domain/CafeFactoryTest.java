package ca.ulaval.glo4002.cafe.small.cafe.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeConfiguration;
import ca.ulaval.glo4002.cafe.domain.CafeFactory;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.bill.BillFactory;
import ca.ulaval.glo4002.cafe.domain.bill.TipRate;
import ca.ulaval.glo4002.cafe.domain.exception.DuplicateCubeNameException;
import ca.ulaval.glo4002.cafe.domain.layout.LayoutFactory;
import ca.ulaval.glo4002.cafe.domain.layout.cube.Cube;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;
import ca.ulaval.glo4002.cafe.domain.location.Country;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.menu.Coffee;
import ca.ulaval.glo4002.cafe.domain.menu.MenuFactory;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.DefaultStrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CafeFactoryTest {
    private static final CafeConfiguration A_CAFE_CONFIGURATION =
        new CafeConfiguration(4, new CafeName("Les 4-Fées"), new DefaultStrategy(), new Location(Country.None, Optional.empty(), Optional.empty()),
            new TipRate(0));
    private static final List<CubeName> DUPLICATE_CUBE_NAMES = List.of(new CubeName("Bob"), new CubeName("Bob"));
    private static final List<CubeName> SOME_CUBE_NAMES = List.of(new CubeName("John"), new CubeName("Bob"));
    private static final Map<CoffeeName, Coffee> AN_EMPTY_MENU = Map.of();
    private CafeFactory cafeFactory;

    @BeforeEach
    public void createCafeFactory() {
        cafeFactory = new CafeFactory(new LayoutFactory(), new MenuFactory(), new BillFactory());
    }

    @Test
    public void whenCreatingCafe_shouldCreateCubesListWithSortedCubesNames() {
        Cafe cafe = cafeFactory.createCafe(SOME_CUBE_NAMES, A_CAFE_CONFIGURATION, AN_EMPTY_MENU);

        assertEquals(SOME_CUBE_NAMES.stream().sorted().toList(), cafe.getLayout().getCubes().stream().map(Cube::getName).toList());
    }

    @Test
    public void whenCreatingCafe_shouldHaveEmptyInventory() {
        Cafe cafe = cafeFactory.createCafe(SOME_CUBE_NAMES, A_CAFE_CONFIGURATION, AN_EMPTY_MENU);

        assertTrue(cafe.getInventory().getIngredients().isEmpty());
    }

    @Test
    public void givenDuplicateCubeNames_whenCreatingCafe_shouldThrowException() {
        assertThrows(DuplicateCubeNameException.class, () -> cafeFactory.createCafe(DUPLICATE_CUBE_NAMES, A_CAFE_CONFIGURATION, AN_EMPTY_MENU));
    }
}
