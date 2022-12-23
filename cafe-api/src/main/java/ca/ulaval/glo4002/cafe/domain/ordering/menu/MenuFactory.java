package ca.ulaval.glo4002.cafe.domain.ordering.menu;

import java.util.Map;

import ca.ulaval.glo4002.cafe.domain.ordering.CoffeeName;

public class MenuFactory {
    public Menu createMenu(Map<CoffeeName, Coffee> coffees) {
        return new Menu(coffees);
    }
}
