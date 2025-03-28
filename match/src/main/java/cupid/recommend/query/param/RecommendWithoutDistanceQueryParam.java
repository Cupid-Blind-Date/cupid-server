package cupid.recommend.query.param;

import cupid.filter.domain.Filter;
import cupid.member.domain.Gender;

public record RecommendWithoutDistanceQueryParam(
        Long memberId,
        Gender targetGender,
        int maxIncludeAge,
        int minIncludeAge,
        boolean permitExcessAge,
        int size
) {
    public static RecommendWithoutDistanceQueryParam of(
            Filter filter,
            int size
    ) {
        return new RecommendWithoutDistanceQueryParam(
                filter.getMemberId(),
                filter.getGenderCondition().getTargetGender(),
                filter.getAgeCondition().getMaxIncludeAge(),
                filter.getAgeCondition().getMinIncludeAge(),
                filter.getAgeCondition().isPermitExcessAge(),
                size
        );
    }
}
