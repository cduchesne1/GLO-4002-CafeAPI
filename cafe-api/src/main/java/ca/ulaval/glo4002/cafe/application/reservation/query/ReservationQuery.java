package ca.ulaval.glo4002.cafe.application.reservation.query;

import ca.ulaval.glo4002.cafe.domain.reservation.GroupName;
import ca.ulaval.glo4002.cafe.domain.reservation.GroupSize;

public record ReservationQuery(GroupName groupName, GroupSize groupSize) {
    public ReservationQuery(String groupName, int groupSize) {
        this(new GroupName(groupName), new GroupSize(groupSize));
    }

    public static ReservationQuery from(String groupName, int groupSize) {
        return new ReservationQuery(groupName, groupSize);
    }
}
