package cupid.member.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class RecentActiveInfo {

    private LocalDateTime lastActiveDate;

    // 위도
    private Double lastActiveLatitude;

    // 위도 (경도)
    private Double lastActiveLongitude;

    public RecentActiveInfo(LocalDateTime lastActiveDate, Double lastActiveLatitude, Double lastActiveLongitude) {
        this.lastActiveDate = lastActiveDate;
        this.lastActiveLatitude = lastActiveLatitude;
        this.lastActiveLongitude = lastActiveLongitude;
    }
}
