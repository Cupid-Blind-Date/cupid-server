package cupid.filter.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 나와의 거리
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DistanceCondition {

    private int maxIncludeDistanceFromMe;
    private boolean permitExcessDistance;

    public DistanceCondition(int maxIncludeDistanceFromMe, boolean permitExcessDistance) {
        this.maxIncludeDistanceFromMe = maxIncludeDistanceFromMe;
        this.permitExcessDistance = permitExcessDistance;
    }
}
