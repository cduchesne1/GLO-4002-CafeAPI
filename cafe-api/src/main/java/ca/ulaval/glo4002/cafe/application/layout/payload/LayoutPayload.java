package ca.ulaval.glo4002.cafe.application.layout.payload;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeName;
import ca.ulaval.glo4002.cafe.domain.layout.cube.Cube;

public record LayoutPayload(CafeName name, List<Cube> cubes) {
    public static LayoutPayload fromCafe(Cafe cafe) {
        return new LayoutPayload(cafe.getName(), cafe.getLayout().getCubes());
    }
}
