package ca.ulaval.glo4002.cafe.api.reservation.assembler;

import java.util.List;

import ca.ulaval.glo4002.cafe.api.reservation.response.ReservationResponse;
import ca.ulaval.glo4002.cafe.application.reservation.payload.ReservationPayload;

public class ReservationResponseAssembler {
    public List<ReservationResponse> toReservationsResponse(ReservationPayload reservationPayload) {
        return reservationPayload.reservations().stream().map(reservation -> new ReservationResponse(reservation.name().value(), reservation.size().value()))
            .toList();
    }
}
