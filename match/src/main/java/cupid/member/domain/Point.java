package cupid.member.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Point {

    // 위도
    private Double lastActiveLatitude;

    // 위도 (경도)
    private Double lastActiveLongitude;

    public Point(Double lastActiveLatitude, Double lastActiveLongitude) {
        this.lastActiveLatitude = lastActiveLatitude;
        this.lastActiveLongitude = lastActiveLongitude;
    }
}
