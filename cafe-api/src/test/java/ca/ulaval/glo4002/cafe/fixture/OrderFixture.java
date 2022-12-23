package ca.ulaval.glo4002.cafe.fixture;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.ordering.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.ordering.Order;

public class OrderFixture {
    private List<CoffeeName> items = List.of(new CoffeeName("Americano"));

    public OrderFixture withItems(List<CoffeeName> items) {
        this.items = items;
        return this;
    }

    public Order build() {
        return new Order(items);
    }
}
