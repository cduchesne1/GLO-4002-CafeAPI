package ca.ulaval.glo4002.cafe.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import ca.ulaval.glo4002.cafe.domain.bill.Bill;
import ca.ulaval.glo4002.cafe.domain.bill.BillFactory;
import ca.ulaval.glo4002.cafe.domain.bill.TipRate;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerNoBillException;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerNotFoundException;
import ca.ulaval.glo4002.cafe.domain.inventory.Inventory;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.Customer;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerId;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.order.Order;

public class PointOfSale {
    private final BillFactory billFactory;
    private final HashMap<CustomerId, Order> orders = new HashMap<>();
    private final HashMap<CustomerId, Bill> bills = new HashMap<>();

    public PointOfSale(BillFactory billFactory) {
        this.billFactory = billFactory;
    }

    public Order getOrderByCustomerId(CustomerId customerId) {
        Optional<Order> customerOrder = Optional.ofNullable(orders.get(customerId));
        return customerOrder.orElseThrow(CustomerNotFoundException::new);
    }

    public Bill getCustomerBill(CustomerId customerId, boolean isSeated) {
        if (!bills.containsKey(customerId)) {
            if (isSeated) {
                throw new CustomerNoBillException();
            } else {
                throw new CustomerNotFoundException();
            }
        }
        return bills.get(customerId);
    }

    public void createEmptyOrderForCustomer(Customer customer) {
        orders.put(customer.getId(), new Order(List.of()));
    }

    public void placeOrder(CustomerId customerId, Order order, Inventory inventory) {
        if (orders.containsKey(customerId)) {
            inventory.useIngredients(order.ingredientsNeeded());
            Order modifiedOrder = orders.get(customerId).addAllItems(order);
            orders.put(customerId, modifiedOrder);
        } else {
            throw new CustomerNotFoundException();
        }
    }

    public boolean customerHasBill(CustomerId customerId) {
        return bills.containsKey(customerId);
    }

    public void createBillForCustomer(CustomerId customerId, Location location, TipRate groupTipRate, boolean isReservedForGroup) {
        Bill bill = billFactory.createBill(orders.get(customerId), location, groupTipRate, isReservedForGroup);
        bills.put(customerId, bill);
        orders.remove(customerId);
    }

    public void clear() {
        orders.clear();
        bills.clear();
    }
}
