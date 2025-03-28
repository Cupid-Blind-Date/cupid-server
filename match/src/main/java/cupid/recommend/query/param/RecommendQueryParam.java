package cupid.recommend.query.param;

import cupid.filter.domain.Filter;
import cupid.member.domain.Gender;
import jakarta.annotation.Nullable;

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
            @Nullable Double latitude,
            @Nullable Double longitude,
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
                latitude,
                longitude,
                size
        );
    }

    public boolean hasLocationInfo() {
        return lat != null && lng != null;
    }
}
