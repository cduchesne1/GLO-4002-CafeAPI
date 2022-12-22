package ca.ulaval.glo4002.cafe.application.reservation.payload;

import java.util.List;

import ca.ulaval.glo4002.cafe.domain.reservation.Reservation;

public record ReservationPayload(List<Reservation> reservations) {
    public ReservationPayload(List<Reservation> reservations) {
        this.reservations = List.copyOf(reservations);
    }
}
