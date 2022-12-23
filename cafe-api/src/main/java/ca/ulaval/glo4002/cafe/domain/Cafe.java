package ca.ulaval.glo4002.cafe.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ca.ulaval.glo4002.cafe.domain.bill.Bill;
import ca.ulaval.glo4002.cafe.domain.bill.TipRate;
import ca.ulaval.glo4002.cafe.domain.exception.CustomerAlreadyVisitedException;
import ca.ulaval.glo4002.cafe.domain.inventory.IngredientType;
import ca.ulaval.glo4002.cafe.domain.inventory.Inventory;
import ca.ulaval.glo4002.cafe.domain.inventory.Quantity;
import ca.ulaval.glo4002.cafe.domain.layout.Layout;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.Seat;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.Customer;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerId;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.menu.Menu;
import ca.ulaval.glo4002.cafe.domain.order.Order;
import ca.ulaval.glo4002.cafe.domain.reservation.BookingRegister;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupName;
import ca.ulaval.glo4002.cafe.domain.reservation.Reservation;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.ReservationStrategy;

public class Cafe {
    private final Layout layout;
    private final Menu menu;
    private final BookingRegister bookingRegister;
    private final PointOfSale pointOfSale;
    private final Inventory inventory;
    private TipRate groupTipRate;
    private int cubeSize;
    private CafeName cafeName;
    private Location location;
    private ReservationStrategy reservationStrategy;

    public Cafe(CafeConfiguration cafeConfiguration, Menu menu, Layout layout, BookingRegister bookingRegister, PointOfSale pointOfSale, Inventory inventory) {
        this.layout = layout;
        this.menu = menu;
        this.bookingRegister = bookingRegister;
        this.pointOfSale = pointOfSale;
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
        return pointOfSale.getOrderByCustomerId(customerId);
    }

    public void updateConfiguration(CafeConfiguration cafeConfiguration) {
        this.cubeSize = cafeConfiguration.cubeSize();
        this.cafeName = cafeConfiguration.cafeName();
        this.reservationStrategy = cafeConfiguration.reservationStrategy();
        this.groupTipRate = cafeConfiguration.groupTipRate();
        this.location = cafeConfiguration.location();
    }

    public void addIngredientsToInventory(Map<IngredientType, Quantity> ingredients) {
        inventory.add(ingredients);
    }

    public void makeReservation(Reservation reservation) {
        bookingRegister.makeReservation(reservation, reservationStrategy, layout.getCubes());
    }

    public void checkIn(Customer customer, Optional<GroupName> groupName) {
        checkIfCustomerAlreadyVisitedToday(customer.getId());
        assignSeatToCustomer(customer, groupName);
        pointOfSale.createEmptyOrderForCustomer(customer);
    }

    private void checkIfCustomerAlreadyVisitedToday(CustomerId customerId) {
        if (pointOfSale.customerHasBill(customerId) || layout.isCustomerAlreadySeated(customerId)) {
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

    public void placeOrder(CustomerId customerId, Order order) {
        pointOfSale.placeOrder(customerId, order, menu.getIngredientsNeeded(order), inventory);
    }

    public void checkOut(CustomerId customerId) {
        Seat seat = layout.getSeatByCustomerId(customerId);
        pointOfSale.createBillForCustomer(customerId, menu, location, groupTipRate, seat.isReservedForGroup());
        layout.checkout(customerId);
    }

    public Bill getCustomerBill(CustomerId customerId) {
        return pointOfSale.getCustomerBill(customerId, layout.isCustomerAlreadySeated(customerId));
    }

    public void close() {
        layout.reset(cubeSize);
        bookingRegister.clearReservations();
        pointOfSale.clear();
        inventory.clear();
    }
}
