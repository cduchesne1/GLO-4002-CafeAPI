package ca.ulaval.glo4002.cafe.api.layout.assembler;

import java.util.List;

import ca.ulaval.glo4002.cafe.api.layout.response.CubeResponse;
import ca.ulaval.glo4002.cafe.api.layout.response.LayoutResponse;
import ca.ulaval.glo4002.cafe.application.layout.payload.LayoutPayload;
import ca.ulaval.glo4002.cafe.domain.layout.cube.Cube;

public class LayoutResponseAssembler {

    public LayoutResponse toLayoutResponse(LayoutPayload layoutPayload) {
        List<CubeResponse> cubeResponses = toCubeResponses(layoutPayload.cubes());
        return new LayoutResponse(layoutPayload.name().value(), cubeResponses);
    }

    private List<CubeResponse> toCubeResponses(List<Cube> cubes) {
        CubeResponseAssembler cubeResponseAssembler = new CubeResponseAssembler();
        return cubes.stream().map(cubeResponseAssembler::toCubeResponse).toList();
    }
}
