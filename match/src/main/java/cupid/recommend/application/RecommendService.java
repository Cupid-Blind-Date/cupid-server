package cupid.recommend.application;

import cupid.common.value.Point;
import cupid.filter.domain.Filter;
import cupid.filter.domain.FilterRepository;
import cupid.member.domain.Member;
import cupid.member.domain.MemberRepository;
import cupid.recommend.cache.RecommendCacheManager;
import cupid.recommend.query.RecommendQuery;
import cupid.recommend.query.param.RecommendByIdsQueryParam;
import cupid.recommend.query.param.RecommendQueryParam;
import cupid.recommend.query.param.RecommendWithoutDistanceQueryParam;
import cupid.recommend.query.result.RecommendedProfile;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecommendService {

    private static final int CACHE_SIZE = 100;

    private final MemberRepository memberRepository;
    private final RecommendCacheManager recommendCacheManager;
    private final RecommendQuery recommendQuery;
    private final FilterRepository filterRepository;

    /**
     * 매칭 대상 추천
     */
    public Optional<RecommendedProfile> recommend(
            Long memberId,
            Point point
    ) {
        // 캐시에서 회원 id 로 후보군 조회
        List<Long> recommendedMemberIds = recommendCacheManager.get(memberId);

        // 캐시에 값이 없는 경우 리로드
        if (recommendedMemberIds.isEmpty()) {
            recommendedMemberIds = reloadCache(memberId, point);
            return recommendRandom(memberId, recommendedMemberIds, point);
        }

        // 캐시에 값이 있는 경우. 해당 캐시에 들어있는 값들 중, 현재에도 필터 조건 만족하는 애들만 조회.
        // 거리정보가 없으면 거리 상관없이 반환
        if (!point.hasLocation()) {
            return recommendRandom(memberId, recommendedMemberIds, point);
        }

        // 캐시에 값이 있고 거리정보가 필요한 경우
        Filter filter = filterRepository.getByMemberId(memberId);
        RecommendByIdsQueryParam param = RecommendByIdsQueryParam.of(recommendedMemberIds, filter, point);
        // 거리조건 재확인하여 부합하는 대상만 남김
        recommendedMemberIds = recommendQuery.findRecommendedByIdsIn(param);
        return recommendRandom(memberId, recommendedMemberIds, point);
    }

    private Optional<RecommendedProfile> recommendRandom(Long memberId, List<Long> recommendedMemberIds, Point point) {
        // 실제 추천할 대상이 없는 경우
        if (recommendedMemberIds.isEmpty()) {
            return Optional.empty();
        }

        // 추천 대상이 있는 경우 셔플 후 한 명 뽑아서 반환한다.
        Collections.shuffle(recommendedMemberIds);
        Long id = recommendedMemberIds.removeFirst();
        recommendCacheManager.update(memberId, recommendedMemberIds);
        Member member = memberRepository.getById(id);
        return Optional.of(RecommendedProfile.of(member, point));
    }

    public List<Long> reloadCache(
            Long memberId,
            Point point
    ) {
        Filter filter = filterRepository.getByMemberId(memberId);
        // 거리 정보 없는 경우
        if (!point.hasLocation()) {
            RecommendWithoutDistanceQueryParam param = RecommendWithoutDistanceQueryParam.of(filter, CACHE_SIZE);
            List<Long> ids = recommendQuery.findRecommendedWithoutDistance(param);
            recommendCacheManager.update(memberId, ids);
            return ids;
        }
        // 거리 정보 있는 경우
        RecommendQueryParam param = RecommendQueryParam.of(filter, point, CACHE_SIZE);
        List<Long> ids = recommendQuery.findRecommended(param);
        recommendCacheManager.update(memberId, ids);
        return ids;
    }
}
