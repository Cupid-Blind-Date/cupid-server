package cupid.recommend.query.param;

import cupid.filter.domain.Filter;

public record RecommendQueryParam(
        Long memberId,
        int maxIncludeAge,
        int minIncludeAge,
        boolean permitExcessAge,
        int maxIncludeDistanceFromMe,
        boolean permitExcessDistance,
        int size
) {
    public static RecommendQueryParam of(Filter filter, int size) {
        return new RecommendQueryParam(
                filter.getMemberId(),
                filter.getAgeCondition().getMaxIncludeAge(),
                filter.getAgeCondition().getMinIncludeAge(),
                filter.getAgeCondition().isPermitExcessAge(),
                filter.getDistanceCondition().getMaxIncludeDistanceFromMe(),
                filter.getDistanceCondition().isPermitExcessDistance(),
                size
        );
    }
}
