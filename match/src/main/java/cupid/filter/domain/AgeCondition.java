package cupid.filter.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AgeCondition {

    private int maxIncludeAge;
    private int minIncludeAge;
    private boolean permitExcessAge;

    public AgeCondition(int maxIncludeAge, int minIncludeAge, boolean permitExcessAge) {
        this.maxIncludeAge = maxIncludeAge;
        this.minIncludeAge = minIncludeAge;
        this.permitExcessAge = permitExcessAge;
    }
}
