package ca.ulaval.glo4002.cafe.fixture;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.cafe.application.reservation.payload.ReservationPayload;
import ca.ulaval.glo4002.cafe.domain.reservation.Reservation;

public class ReservationPayloadFixture {
    List<Reservation> reservations = new ArrayList<>();

    public ReservationPayloadFixture withReservation(List<Reservation> reservations) {
        this.reservations = reservations;
        return this;
    }

    public ReservationPayload build() {
        return new ReservationPayload(reservations);
    }
}
