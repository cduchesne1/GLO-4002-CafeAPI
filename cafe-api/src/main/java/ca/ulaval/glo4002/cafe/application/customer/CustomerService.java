package ca.ulaval.glo4002.cafe.application.customer;

import ca.ulaval.glo4002.cafe.application.customer.payload.BillPayload;
import ca.ulaval.glo4002.cafe.application.customer.payload.CustomerPayload;
import ca.ulaval.glo4002.cafe.application.customer.payload.OrderPayload;
import ca.ulaval.glo4002.cafe.application.customer.query.CheckInCustomerQuery;
import ca.ulaval.glo4002.cafe.application.customer.query.CheckOutCustomerQuery;
import ca.ulaval.glo4002.cafe.application.customer.query.CustomerOrderQuery;
import ca.ulaval.glo4002.cafe.domain.Cafe;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.bill.Bill;
import ca.ulaval.glo4002.cafe.domain.bill.BillFactory;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.Seat;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.Customer;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerFactory;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerId;
import ca.ulaval.glo4002.cafe.domain.order.Order;

public class CustomerService {
    private final CafeRepository cafeRepository;
    private final CustomerFactory customerFactory;
    private final BillFactory billFactory;

    public CustomerService(CafeRepository cafeRepository, CustomerFactory customerFactory, BillFactory billFactory) {
        this.cafeRepository = cafeRepository;
        this.customerFactory = customerFactory;
        this.billFactory = billFactory;
    }

    public CustomerPayload getCustomer(CustomerId customerId) {
        Cafe cafe = cafeRepository.get();
        Seat seat = cafe.getSeatByCustomerId(customerId);

        return CustomerPayload.fromSeat(seat);
    }

    public void checkIn(CheckInCustomerQuery checkInCustomerQuery) {
        Cafe cafe = cafeRepository.get();

        Customer customer = customerFactory.createCustomer(checkInCustomerQuery.customerId(), checkInCustomerQuery.customerName());
        cafe.checkIn(customer, checkInCustomerQuery.groupName());

        cafeRepository.saveOrUpdate(cafe);
    }

    public OrderPayload getOrder(CustomerId customerId) {
        Cafe cafe = cafeRepository.get();

        Order order = cafe.getOrderByCustomerId(customerId);

        return OrderPayload.fromOrder(order);
    }

    public void checkOut(CheckOutCustomerQuery checkOutCustomerQuery) {
        Cafe cafe = cafeRepository.get();
        cafe.checkOut(checkOutCustomerQuery.customerId());
        cafeRepository.saveOrUpdate(cafe);
    }

    public BillPayload getCustomerBill(CustomerId customerId) {
        Cafe cafe = cafeRepository.get();
        Bill bill = cafe.getCustomerBill(customerId);

        return BillPayload.fromBill(bill);
    }

    public void placeOrder(CustomerOrderQuery customerOrderQuery) {
        Cafe cafe = cafeRepository.get();
        cafe.placeOrder(customerOrderQuery.customerId(), customerOrderQuery.order());
        cafeRepository.saveOrUpdate(cafe);
    }
}
