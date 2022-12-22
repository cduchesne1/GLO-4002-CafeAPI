package ca.ulaval.glo4002.cafe.domain;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;

public class CafeFactory {
    public Cafe createCafe(List<CubeName> cubeNames, CafeConfiguration cafeConfiguration) {
        return new Cafe(cubeNames, cafeConfiguration);
    }
}
