package cupid.recommend.query.param;

import cupid.common.value.Point;
import cupid.filter.domain.Filter;
import cupid.member.domain.Gender;

public record RecommendQueryParam(
        Long memberId,
        Gender targetGender,
        int maxIncludeAge,
        int minIncludeAge,
        boolean permitExcessAge,
        int maxIncludeDistanceFromMe, // 나와의 거리
        boolean permitExcessDistance,
        Double lat,  // 위도
        Double lng,  // 경도
        int size
) {
    public static RecommendQueryParam of(
            Filter filter,
            Point point,
            int size
    ) {
        return new RecommendQueryParam(
                filter.getMemberId(),
                filter.getGenderCondition().getTargetGender(),
                filter.getAgeCondition().getMaxIncludeAge(),
                filter.getAgeCondition().getMinIncludeAge(),
                filter.getAgeCondition().isPermitExcessAge(),
                filter.getDistanceCondition().getMaxIncludeDistanceFromMe(),
                filter.getDistanceCondition().isPermitExcessDistance(),
                point.getLatitude().doubleValue(),
                point.getLongitude().doubleValue(),
                size
        );
    }
}
