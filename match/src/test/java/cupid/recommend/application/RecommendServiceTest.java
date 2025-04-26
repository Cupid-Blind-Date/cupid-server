package cupid.recommend.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import cupid.common.value.Point;
import cupid.filter.domain.AgeCondition;
import cupid.filter.domain.DistanceCondition;
import cupid.filter.domain.Filter;
import cupid.filter.domain.FilterRepository;
import cupid.filter.domain.GenderCondition;
import cupid.member.domain.Gender;
import cupid.member.domain.Member;
import cupid.member.domain.MemberRepository;
import cupid.member.domain.RepresentativeProfileImage;
import cupid.recommend.cache.RecommendCacheManager;
import cupid.recommend.query.RecommendQuery;
import cupid.recommend.query.result.RecommendedProfile;
import cupid.support.ApplicationTest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DisplayName("추천 서비스 (RecommendService) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RecommendServiceTest extends ApplicationTest {

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private RecommendCacheManager recommendCacheManager;

    @MockitoBean
    private RecommendQuery recommendQuery;

    @MockitoBean
    private FilterRepository filterRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member1 = new Member("u1", "p", "n", 10, Gender.FEMALE, new RepresentativeProfileImage("1", "2"));
    private Member member2 = new Member("u2", "p", "n", 10, Gender.FEMALE, new RepresentativeProfileImage("3", "4"));
    private Member member3 = new Member("u3", "p", "n", 10, Gender.FEMALE, new RepresentativeProfileImage("5", "6"));
    private Member member4 = new Member("u4", "p", "n", 10, Gender.FEMALE, new RepresentativeProfileImage("7", "8"));

    @BeforeEach
    void setUp() {
        recommendCacheManager.deleteAll();
        memberRepository.saveAll(List.of(
                member1,
                member2,
                member3,
                member4
        ));
    }

    @AfterEach
    void tearDown() {
        recommendCacheManager.deleteAll();
    }

    @Test
    void 추천_시_캐시에_값이_있으며_거리정보가_없는_경우_캐시의_값을_반환한다() {
        // given
        recommendCacheManager.update(1L, new ArrayList<>(List.of(member1.getId(), member2.getId())));

        // when
        Optional<RecommendedProfile> recommend = recommendService.recommend(1L, new Point());

        // then
        assertThat(recommend).isPresent();
        assertThat(recommend.get().id()).isIn(member1.getId(), member2.getId());
    }

    @Test
    void 추천_시_캐시에_값이_있으며_거리정보가_있는_경우_캐시에_있는_값들_중에서_거리필터에_부합하는_회원만_조회한다() {
        // given
        recommendCacheManager.update(1L, new ArrayList<>(List.of(member1.getId(), member2.getId())));
        given(filterRepository.getByMemberId(1L))
                .willReturn(new Filter(1L,
                                new AgeCondition(10, 20, true),
                                new DistanceCondition(10, true),
                                GenderCondition.ONLY_MALE
                        )
                );
        given(recommendQuery.findRecommendedByIdsIn(any()))
                .willReturn(new ArrayList<>(List.of(member1.getId())));

        // when
        Optional<RecommendedProfile> recommend = recommendService.recommend(1L, new Point(1.1, 2.2));

        // then
        assertThat(recommend).isPresent();
        assertThat(recommend.get().id()).isIn(member1.getId(), member2.getId());
    }

    @Test
    void 추천_시_캐시에_값이_있으나_거리필터에_부합하는_회원이_캐시_내에_없다면_DB에서_조회한다() {
        // given
        recommendCacheManager.update(1L, new ArrayList<>(List.of(member1.getId(), member2.getId())));
        given(filterRepository.getByMemberId(1L))
                .willReturn(new Filter(1L,
                                new AgeCondition(10, 20, true),
                                new DistanceCondition(10, true),
                                GenderCondition.ONLY_MALE
                        )
                );
        given(recommendQuery.findRecommendedByIdsIn(any()))
                .willReturn(new ArrayList<>());
        given(recommendQuery.findRecommended(any()))
                .willReturn(new ArrayList<>(List.of(member3.getId())));


        // when
        Optional<RecommendedProfile> recommend = recommendService.recommend(1L, new Point(1.1, 2.2));

        // then
        assertThat(recommend).isPresent();
        assertThat(recommend.get().id()).isEqualTo(member3.getId());
    }

    @Test
    void 추천_시_캐시에_값이_없다면_DB에서_조회하고_캐시한다() {
        // given
        given(filterRepository.getByMemberId(1L))
                .willReturn(new Filter(1L,
                                new AgeCondition(10, 20, true),
                                new DistanceCondition(10, true),
                                GenderCondition.ONLY_FEMALE
                        )
                );
        given(recommendQuery.findRecommendedWithoutDistance(any()))
                .willReturn(new ArrayList<>(List.of(member3.getId(), member3.getId())));

        // when
        Optional<RecommendedProfile> recommend = recommendService.recommend(1L, new Point());

        // then
        assertThat(recommend).isPresent();
        assertThat(recommend.get().id()).isIn(member3.getId(), member3.getId());
    }

    @Test
    void 추천_시_캐시에_값이_없고_DB에도_더이상_추천할_회원이_없다면_빈_리스트_반환() {
        // given
        given(filterRepository.getByMemberId(1L))
                .willReturn(new Filter(1L,
                                new AgeCondition(10, 20, true),
                                new DistanceCondition(10, true),
                                GenderCondition.ONLY_MALE
                        )
                );
        given(recommendQuery.findRecommendedWithoutDistance(any()))
                .willReturn(Collections.emptyList());

        // when
        Optional<RecommendedProfile> recommend = recommendService.recommend(1L, new Point());

        // then
        assertThat(recommend).isEmpty();
    }
}
