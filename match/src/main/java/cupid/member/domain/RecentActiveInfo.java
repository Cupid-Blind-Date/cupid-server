package cupid.member.domain;

import cupid.common.value.Point;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class RecentActiveInfo {

    private LocalDateTime lastActiveDate;

    @Embedded
    private Point point;

    public RecentActiveInfo(LocalDateTime lastActiveDate, Point point) {
        this.lastActiveDate = lastActiveDate;
        this.point = point;
    }

    public Point getPoint() {
        return point == null ? new Point() : point;
    }
}
