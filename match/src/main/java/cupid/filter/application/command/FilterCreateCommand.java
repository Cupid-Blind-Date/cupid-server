package cupid.filter.application.command;

import cupid.filter.domain.AgeCondition;
import cupid.filter.domain.DistanceCondition;
import cupid.filter.domain.Filter;
import cupid.filter.domain.GenderCondition;

public record FilterCreateCommand(
        Long memberId,
        int maxIncludeAge,
        int minIncludeAge,
        boolean permitExcessAge,
        int maxIncludeDistanceFromMe,
        boolean permitExcessDistance,
        GenderCondition genderCondition
) {
    public Filter toFilter() {
        return new Filter(
                memberId,
                new AgeCondition(maxIncludeAge, minIncludeAge, permitExcessAge),
                new DistanceCondition(maxIncludeDistanceFromMe, permitExcessDistance),
                genderCondition
        );
    }
}
