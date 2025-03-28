package cupid.recommend.query.param;

import cupid.common.value.Point;
import cupid.filter.domain.Filter;
import java.util.List;

public record RecommendByIdsQueryParam(
        Long memberId,
        List<Long> memberIds,
        int maxIncludeDistanceFromMe, // 나와의 거리
        boolean permitExcessDistance,
        Double lat,  // 위도
        Double lng  // 경도
) {
    public static RecommendByIdsQueryParam of(
            List<Long> memberIds,
            Filter filter,
            Point point
    ) {
        return new RecommendByIdsQueryParam(
                filter.getMemberId(),
                memberIds,
                filter.getDistanceCondition().getMaxIncludeDistanceFromMe(),
                filter.getDistanceCondition().isPermitExcessDistance(),
                point.getLatitude(),
                point.getLongitude()
        );
    }
}
