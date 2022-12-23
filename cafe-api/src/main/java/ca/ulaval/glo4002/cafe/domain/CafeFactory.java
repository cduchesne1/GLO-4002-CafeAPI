package ca.ulaval.glo4002.cafe.domain;

import java.util.List;
import java.util.Map;

import ca.ulaval.glo4002.cafe.domain.bill.BillFactory;
import ca.ulaval.glo4002.cafe.domain.inventory.Inventory;
import ca.ulaval.glo4002.cafe.domain.layout.LayoutFactory;
import ca.ulaval.glo4002.cafe.domain.layout.cube.CubeName;
import ca.ulaval.glo4002.cafe.domain.menu.Coffee;
import ca.ulaval.glo4002.cafe.domain.menu.MenuFactory;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.reservation.BookingRegister;

public class CafeFactory {
    private final LayoutFactory layoutFactory;
    private final MenuFactory menuFactory;
    private final BillFactory billFactory;

    public CafeFactory(LayoutFactory layoutFactory, MenuFactory menuFactory, BillFactory billFactory) {
        this.layoutFactory = layoutFactory;
        this.menuFactory = menuFactory;
        this.billFactory = billFactory;
    }

    public Cafe createCafe(List<CubeName> cubeNames, CafeConfiguration cafeConfiguration, Map<CoffeeName, Coffee> menu) {
        BookingRegister bookingRegister = new BookingRegister();
        PointOfSale pointOfSale = new PointOfSale(billFactory);
        return new Cafe(cafeConfiguration, menuFactory.createMenu(menu), layoutFactory.createLayout(cafeConfiguration.cubeSize(), cubeNames),
            bookingRegister, pointOfSale, new Inventory());
    }
}
