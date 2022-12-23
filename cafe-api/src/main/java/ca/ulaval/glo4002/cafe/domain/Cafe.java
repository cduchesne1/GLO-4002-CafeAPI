package ca.ulaval.glo4002.cafe.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import ca.ulaval.glo4002.cafe.domain.bill.Bill;
import ca.ulaval.glo4002.cafe.domain.bill.BillFactory;
import ca.ulaval.glo4002.cafe.domain.bill.TipRate;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerAlreadyVisitedException;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerNoBillException;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerNotFoundException;
import ca.ulaval.glo4002.cafe.domain.inventory.Ingredient;
import ca.ulaval.glo4002.cafe.domain.inventory.Inventory;
import ca.ulaval.glo4002.cafe.domain.layout.Layout;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.Seat;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.Customer;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerId;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.order.Order;
import ca.ulaval.glo4002.cafe.domain.reservation.BookingRegister;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupName;
import ca.ulaval.glo4002.cafe.domain.reservation.Reservation;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.ReservationStrategy;

public class Cafe {
    private final Layout layout;
    private final BillFactory billFactory;
    private final BookingRegister bookingRegister = new BookingRegister();
    private final HashMap<CustomerId, Bill> bills = new HashMap<>();
    private final HashMap<CustomerId, Order> orders = new HashMap<>();
    private final Inventory inventory;
    private TipRate groupTipRate;
    private int cubeSize;
    private CafeName cafeName;
    private Location location;
    private ReservationStrategy reservationStrategy;

    public Cafe(CafeConfiguration cafeConfiguration, Layout layout, BillFactory billFactory, Inventory inventory) {
        this.layout = layout;
        this.billFactory = billFactory;
        this.inventory = inventory;
        updateConfiguration(cafeConfiguration);
    }

    public CafeName getName() {
        return cafeName;
    }

    public Layout getLayout() {
        return layout;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Reservation> getReservations() {
        return bookingRegister.getReservations();
    }

    public Seat getSeatByCustomerId(CustomerId customerId) {
        return layout.getSeatByCustomerId(customerId);
    }

    public Order getOrderByCustomerId(CustomerId customerId) {
        Optional<Order> customerOrder = Optional.ofNullable(orders.get(customerId));
        return customerOrder.orElseThrow(CustomerNotFoundException::new);
    }

    public void updateConfiguration(CafeConfiguration cafeConfiguration) {
        this.cubeSize = cafeConfiguration.cubeSize();
        this.cafeName = cafeConfiguration.cafeName();
        this.reservationStrategy = cafeConfiguration.reservationStrategy();
        this.groupTipRate = cafeConfiguration.groupTipRate();
        this.location = cafeConfiguration.location();
    }

    public void addIngredientsToInventory(List<Ingredient> ingredients) {
        inventory.add(ingredients);
    }

    public void makeReservation(Reservation reservation) {
        bookingRegister.makeReservation(reservation, reservationStrategy, layout.getCubes());
    }

    public void checkIn(Customer customer, Optional<GroupName> groupName) {
        checkIfCustomerAlreadyVisitedToday(customer.getId());
        assignSeatToCustomer(customer, groupName);
        createEmptyOrderForCustomer(customer);
    }

    private void checkIfCustomerAlreadyVisitedToday(CustomerId customerId) {
        if (bills.containsKey(customerId) || layout.isCustomerAlreadySeated(customerId)) {
            throw new CustomerAlreadyVisitedException();
        }
    }

    private void assignSeatToCustomer(Customer customer, Optional<GroupName> groupName) {
        if (groupName.isPresent()) {
            bookingRegister.validateHasReservation(groupName.get());
            layout.assignSeatToGroupMember(customer, groupName.get());
        } else {
            layout.assignSeatToIndividual(customer);
        }
    }

    private void createEmptyOrderForCustomer(Customer customer) {
        orders.put(customer.getId(), new Order(List.of()));
    }

    public void placeOrder(CustomerId customerId, Order order) {
        if (orders.containsKey(customerId)) {
            inventory.useIngredients(order.ingredientsNeeded());
            Order modifiedOrder = orders.get(customerId).addAllItems(order);
            orders.put(customerId, modifiedOrder);
        } else {
            throw new CustomerNotFoundException();
        }
    }

    public void checkOut(CustomerId customerId) {
        Seat seat = layout.getSeatByCustomerId(customerId);
        Bill bill = billFactory.createBill(orders.get(customerId), location, groupTipRate, seat.isReservedForGroup());
        bills.put(customerId, bill);
        orders.remove(customerId);
        layout.checkout(customerId);
    }

    public Bill getCustomerBill(CustomerId customerId) {
        if (!bills.containsKey(customerId)) {
            if (layout.isCustomerAlreadySeated(customerId)) {
                throw new CustomerNoBillException();
            } else {
                throw new CustomerNotFoundException();
            }
        }
        return bills.get(customerId);
    }

    public void close() {
        layout.reset(cubeSize);
        bookingRegister.clearReservations();
        bills.clear();
        orders.clear();
        inventory.clear();
    }
}
