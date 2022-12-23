package ca.ulaval.glo4002.cafe.domain;

import ca.ulaval.glo4002.cafe.domain.billing.TipRate;
import ca.ulaval.glo4002.cafe.domain.location.Location;
import ca.ulaval.glo4002.cafe.domain.reservation.strategies.ReservationStrategy;

public record CafeConfiguration(int cubeSize, CafeName cafeName, ReservationStrategy reservationStrategy,
                                Location location, TipRate groupTipRate) {
}
