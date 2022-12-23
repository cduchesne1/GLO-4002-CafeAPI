package ca.ulaval.glo4002.cafe.domain.order;

import ca.ulaval.glo4002.cafe.domain.Amount;

public record Coffee(CoffeeType coffeeType) {
    public Amount price() {
        return coffeeType.getPrice();
    }

    public Recipe recipe() {
        return coffeeType.getRecipe();
    }
}
