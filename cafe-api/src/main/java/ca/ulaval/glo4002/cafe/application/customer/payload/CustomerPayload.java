package ca.ulaval.glo4002.cafe.application.customer.payload;

import java.util.Optional;

import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.Seat;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.SeatNumber;
import ca.ulaval.glo4002.cafe.domain.layout.cube.seat.customer.CustomerName;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupName;

public record CustomerPayload(CustomerName name, SeatNumber seatNumber, Optional<GroupName> groupName) {
    public static CustomerPayload fromSeat(Seat seat) {
        return new CustomerPayload(seat.getCustomer().get().getName(), seat.getNumber(), seat.getGroupName());
    }
}
