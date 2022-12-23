package ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer;

import ca.ulaval.glo4002.cafe.domain.TipRate;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.bill.Bill;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.bill.BillFactory;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.order.Order;

public class Customer {
    private final CustomerId id;
    private final CustomerName name;
    private final BillFactory billFactory;

    public Customer(CustomerId customerId, CustomerName customerName) {
        this.id = customerId;
        this.name = customerName;
        this.billFactory = new BillFactory();
    }

    public CustomerId getId() {
        return id;
    }

    public CustomerName getName() {
        return name;
    }

    public Bill createBill(Location location, TipRate groupTipRate, boolean isInGroup, Order order) {
        return billFactory.createBill(order, location, groupTipRate, isInGroup);
    }
}
