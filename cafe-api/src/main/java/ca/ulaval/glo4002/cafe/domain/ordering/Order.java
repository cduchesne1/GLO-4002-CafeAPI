package ca.ulaval.glo4002.cafe.domain.ordering;

import java.util.List;
import java.util.stream.Stream;

public record Order(List<CoffeeName> items) {
    public Order addAllItems(Order otherOrder) {
        return new Order(Stream.concat(items().stream(), otherOrder.items().stream()).toList());
    }
}
