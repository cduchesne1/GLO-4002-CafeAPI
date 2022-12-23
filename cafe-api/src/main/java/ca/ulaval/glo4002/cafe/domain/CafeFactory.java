package ca.ulaval.glo4002.cafe.domain;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.bill.BillFactory;
import ca.ulaval.glo4002.cafe.domain.inventory.Inventory;
import ca.ulaval.glo4002.cafe.domain.layout.LayoutFactory;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;

public class CafeFactory {
    private final LayoutFactory layoutFactory;
    private final BillFactory billFactory;

    public CafeFactory(LayoutFactory layoutFactory, BillFactory billFactory) {
        this.layoutFactory = layoutFactory;
        this.billFactory = billFactory;
    }

    public Cafe createCafe(List<CubeName> cubeNames, CafeConfiguration cafeConfiguration) {
        return new Cafe(cafeConfiguration, layoutFactory.createLayout(cafeConfiguration.cubeSize(), cubeNames), billFactory, new Inventory());
    }
}
