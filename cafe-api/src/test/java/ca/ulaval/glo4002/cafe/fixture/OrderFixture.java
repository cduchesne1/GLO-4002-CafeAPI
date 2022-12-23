package ca.ulaval.glo4002.cafe.fixture;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.order.CoffeeName;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeType;
import ca.ulaval.glo4002.cafe.domain.order.Order;

public class OrderFixture {
    private List<CoffeeName> items = List.of(new CoffeeName(CoffeeType.Americano.toString()));

    public OrderFixture withItems(List<CoffeeName> items) {
        this.items = items;
        return this;
    }

    public Order build() {
        return new Order(items);
    }
}
