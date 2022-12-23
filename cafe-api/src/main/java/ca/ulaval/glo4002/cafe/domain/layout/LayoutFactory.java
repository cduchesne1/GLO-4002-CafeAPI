package ca.ulaval.glo4002.cafe.domain.layout;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;

public class LayoutFactory {
    public Layout createLayout(int cubeSize, List<CubeName> cubeNames) {
        return new Layout(cubeSize, cubeNames);
    }
}
