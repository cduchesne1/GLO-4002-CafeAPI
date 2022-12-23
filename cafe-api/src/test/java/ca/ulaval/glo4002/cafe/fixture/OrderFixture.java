package ca.ulaval.glo4002.cafe.fixture;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.order.Coffee;
import ca.ulaval.glo4002.cafe.domain.order.CoffeeType;
import ca.ulaval.glo4002.cafe.domain.order.Order;

public class OrderFixture {
    private List<Coffee> items = List.of(new Coffee(CoffeeType.Americano));

    public OrderFixture withItems(List<Coffee> items) {
        this.items = items;
        return this;
    }

    public Order build() {
        return new Order(items);
    }
}
