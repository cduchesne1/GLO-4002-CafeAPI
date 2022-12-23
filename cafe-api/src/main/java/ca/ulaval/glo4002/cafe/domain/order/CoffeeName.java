package ca.ulaval.glo4002.cafe.domain.order;

public record CoffeeName(String value) {
    public CoffeeName {
        CoffeeType.fromString(value);
    }
}
