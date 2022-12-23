package ca.ulaval.glo4002.cafe.api.configuration.request;

import ca.ulaval.glo4002.cafe.api.inventory.request.InventoryRequest;

import jakarta.validation.constraints.NotNull;

public class UpdateMenuRequest {
    @NotNull(message = "The name may not be null.")
    public String name;
    @NotNull(message = "The ingredients may not be null.")
    public InventoryRequest ingredients;
    public float cost;
}
