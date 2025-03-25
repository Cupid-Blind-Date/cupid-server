package cupid.recommend.application;

import cupid.filter.domain.Filter;
import cupid.filter.domain.FilterRepository;
import cupid.member.domain.Member;
import cupid.member.domain.MemberRepository;
import cupid.recommend.cache.RecommendCacheManager;
import cupid.recommend.query.RecommendQuery;
import cupid.recommend.query.param.RecommendQueryParam;
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
    public Optional<RecommendedProfile> recommend(Long memberId) {
        // 캐시에서 회원 id 로 후보군 조회
        List<Long> recommendedMemberIds = recommendCacheManager.get(memberId);

        // 캐시에 값이 없는 경우 리로드
        if (recommendedMemberIds.isEmpty()) {
            recommendedMemberIds = reloadCache(memberId);
        }

        // 실제 추천할 대상이 없는 경우
        if (recommendedMemberIds.isEmpty()) {
            return Optional.empty();
        }

        // TODO 거리 조건 검사
        // 추천 대상이 있는 경우 셔플 후 한 명 뽑아서 반환한다.
        Collections.shuffle(recommendedMemberIds);
        Long id = recommendedMemberIds.removeFirst();
        recommendCacheManager.update(memberId, recommendedMemberIds);
        Member member = memberRepository.getById(id);
        return Optional.of(RecommendedProfile.from(member));
    }

    public List<Long> reloadCache(Long memberId) {
        Filter filter = filterRepository.getByMemberId(memberId);
        RecommendQueryParam param = RecommendQueryParam.of(filter, CACHE_SIZE);
        List<Long> ids = switch (filter.getGenderCondition()) {
            case ONLY_FEMALE -> recommendQuery.findFemaleRecommended(param);
            case ONLY_MALE -> recommendQuery.findMaleRecommended(param);
            case BOTH -> recommendQuery.findBothGenderRecommended(param);
        };
        recommendCacheManager.update(memberId, ids);
        return ids;
    }
}
