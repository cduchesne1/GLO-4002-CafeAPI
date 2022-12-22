package ca.ulaval.glo4002.cafe.small.cafe.api.reservation.assembler;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.reservation.assembler.ReservationResponseAssembler;
import ca.ulaval.glo4002.cafe.api.reservation.response.ReservationResponse;
import ca.ulaval.glo4002.cafe.application.reservation.payload.ReservationPayload;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupName;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupSize;
import ca.ulaval.glo4002.cafe.domain.reservation.Reservation;
import ca.ulaval.glo4002.cafe.fixture.ReservationFixture;
import ca.ulaval.glo4002.cafe.fixture.ReservationPayloadFixture;
import ca.ulaval.glo4002.cafe.fixture.ReservationResponseFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReservationResponseAssemblerTest {
    private static final GroupName A_GROUP_NAME = new GroupName("Group1");
    private static final GroupSize A_GROUP_SIZE = new GroupSize(2);
    private static final GroupName ANOTHER_GROUP_NAME = new GroupName("Group2");
    private static final GroupSize ANOTHER_GROUP_SIZE = new GroupSize(4);
    private static final List<Reservation> RESERVATIONS = List.of(new ReservationFixture().withGroupName(A_GROUP_NAME).withGroupSize(A_GROUP_SIZE).build(),
        new ReservationFixture().withGroupName(ANOTHER_GROUP_NAME).withGroupSize(ANOTHER_GROUP_SIZE).build());

    private ReservationResponseAssembler reservationResponseAssembler;

    @BeforeEach
    public void createAssembler() {
        reservationResponseAssembler = new ReservationResponseAssembler();
    }

    @Test
    public void givenReservationPayloadWithNoReservation_whenAssemblingGroupsResponse_shouldReturnEmptyList() {
        ReservationPayload reservationPayload = new ReservationPayloadFixture().build();

        List<ReservationResponse> reservationResponse = reservationResponseAssembler.toReservationsResponse(reservationPayload);

        assertTrue(reservationResponse.isEmpty());
    }

    @Test
    public void givenReservationPayloadWithReservations_whenAssemblingGroupsResponse_shouldReturnListOfGroupResponse() {
        ReservationPayload reservationPayload = new ReservationPayloadFixture().withReservation(RESERVATIONS).build();
        List<ReservationResponse> expectedResponse =
            List.of(new ReservationResponseFixture().withGroupName(A_GROUP_NAME.value()).withGroupSize(A_GROUP_SIZE.value()).build(),
                new ReservationResponseFixture().withGroupName(ANOTHER_GROUP_NAME.value()).withGroupSize(ANOTHER_GROUP_SIZE.value()).build());

        List<ReservationResponse> reservationResponse = reservationResponseAssembler.toReservationsResponse(reservationPayload);

        assertEquals(expectedResponse, reservationResponse);
    }
}
