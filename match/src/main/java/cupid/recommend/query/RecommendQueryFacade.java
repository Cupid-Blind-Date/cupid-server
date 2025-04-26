package cupid.recommend.query;

import cupid.common.value.Point;
import cupid.filter.domain.Filter;
import cupid.filter.domain.FilterRepository;
import cupid.recommend.query.param.RecommendByIdsQueryParam;
import cupid.recommend.query.param.RecommendQueryParam;
import cupid.recommend.query.param.RecommendWithoutDistanceQueryParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RecommendQueryFacade {

    private final RecommendQuery recommendQuery;
    private final FilterRepository filterRepository;

    public List<Long> findRecommends(
            Long memberId,
            Point point,
            int size
    ) {
        Filter filter = filterRepository.getByMemberId(memberId);
        // 거리 정보 없는 경우
        if (!point.hasLocation()) {
            RecommendWithoutDistanceQueryParam param = RecommendWithoutDistanceQueryParam.of(filter, size);
            return recommendQuery.findRecommendedWithoutDistance(param);
        }
        // 거리 정보 있는 경우
        RecommendQueryParam param = RecommendQueryParam.of(filter, point, size);
        return recommendQuery.findRecommended(param);
    }

    public List<Long> findRecommendedByIdsIn(Long memberId, List<Long> candidateIds, Point point) {
        Filter filter = filterRepository.getByMemberId(memberId);
        RecommendByIdsQueryParam param = RecommendByIdsQueryParam.of(candidateIds, filter, point);
        return recommendQuery.findRecommendedByIdsIn(param);
    }
}
