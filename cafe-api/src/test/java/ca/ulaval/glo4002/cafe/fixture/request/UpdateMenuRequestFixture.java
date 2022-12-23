package ca.ulaval.glo4002.cafe.fixture.request;

import ca.ulaval.glo4002.cafe.api.configuration.request.UpdateMenuRequest;
import ca.ulaval.glo4002.cafe.api.inventory.request.InventoryRequest;

public class UpdateMenuRequestFixture {
    public String name = "Pumpkin Latte";
    public InventoryRequest ingredients = new InventoryRequestFixture().withEspresso(50).withMilk(50).build();
    public float cost = 4.0f;

    public UpdateMenuRequestFixture withName(String name) {
        this.name = name;
        return this;
    }

    public UpdateMenuRequestFixture withIngredients(InventoryRequest ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public UpdateMenuRequestFixture withCost(float cost) {
        this.cost = cost;
        return this;
    }

    public UpdateMenuRequest build() {
        UpdateMenuRequest updateMenuRequest = new UpdateMenuRequest();
        updateMenuRequest.name = name;
        updateMenuRequest.ingredients = ingredients;
        updateMenuRequest.cost = cost;
        return updateMenuRequest;
    }
}
