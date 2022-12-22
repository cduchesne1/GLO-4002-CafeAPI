package ca.ulaval.glo4002.cafe.medium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.application.reservation.ReservationService;
import ca.ulaval.glo4002.cafe.application.reservation.payload.ReservationPayload;
import ca.ulaval.glo4002.cafe.application.reservation.query.ReservationQuery;
import ca.ulaval.glo4002.cafe.domain.CafeRepository;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupName;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupSize;
import ca.ulaval.glo4002.cafe.domain.reservation.ReservationFactory;
import ca.ulaval.glo4002.cafe.util.CafeRepositoryTestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationServiceTest {
    private static final GroupSize A_GROUP_SIZE = new GroupSize(4);
    private static final GroupName A_GROUP_NAME = new GroupName("My group");
    private static final ReservationQuery A_RESERVATION_QUERY = new ReservationQuery(A_GROUP_NAME.value(), A_GROUP_SIZE.value());

    private ReservationService reservationService;

    @BeforeEach
    public void instanciateAttributes() {
        CafeRepository cafeRepository = CafeRepositoryTestUtil.createCafeRepositoryWithDefaultCafe();
        reservationService = new ReservationService(cafeRepository, new ReservationFactory());
    }

    @Test
    public void whenMakingReservation_shouldSaveReservation() {
        reservationService.makeReservation(A_RESERVATION_QUERY);
        ReservationPayload actualReservationPayload = reservationService.getReservations();

        assertEquals(A_GROUP_SIZE, actualReservationPayload.reservations().get(0).size());
        assertEquals(A_GROUP_NAME, actualReservationPayload.reservations().get(0).name());
    }
}
