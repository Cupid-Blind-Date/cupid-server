package cupid.recommend.query;

import static cupid.fixture.MemberFixture.member;
import static cupid.member.domain.Gender.FEMALE;
import static cupid.member.domain.Gender.MALE;
import static org.assertj.core.api.Assertions.assertThat;

import cupid.common.value.Point;
import cupid.member.domain.Member;
import cupid.member.domain.MemberRepository;
import cupid.member.domain.RecentActiveInfo;
import cupid.recommend.query.param.RecommendByIdsQueryParam;
import cupid.recommend.query.param.RecommendQueryParam;
import cupid.recommend.query.param.RecommendWithoutDistanceQueryParam;
import cupid.support.ApplicationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("RecommendQuery 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RecommendQueryTest extends ApplicationTest {

    @Autowired
    private RecommendQuery recommendQuery;

    @Autowired
    private MemberRepository memberRepository;

    private Member member1 = member(10, FEMALE);
    private Member member2 = member(20, FEMALE);
    private Member member3 = member(30, FEMALE);
    private Member member4 = member(40, FEMALE);
    private Member member5 = member(50, FEMALE);
    private Member member6 = member(10, MALE);
    private Member member7 = member(20, MALE);
    private Member member8 = member(30, MALE);
    private Member member9 = member(40, MALE);

    @BeforeEach
    void setUp() {
        member1.updateRecentActiveInfo(new RecentActiveInfo(LocalDateTime.now(), new Point(0.0, 0.0)));
        member2.updateRecentActiveInfo(new RecentActiveInfo(LocalDateTime.now(), new Point(0.0, 0.0)));
        member3.updateRecentActiveInfo(new RecentActiveInfo(LocalDateTime.now(), new Point(0.0, 0.0)));
        member4.updateRecentActiveInfo(new RecentActiveInfo(LocalDateTime.now(), new Point(0.0, 0.0)));
        member5.updateRecentActiveInfo(new RecentActiveInfo(LocalDateTime.now(), new Point(0.0, 0.0)));
        member6.updateRecentActiveInfo(new RecentActiveInfo(LocalDateTime.now(), new Point(0.0, 0.0)));
        member7.updateRecentActiveInfo(new RecentActiveInfo(LocalDateTime.now(), new Point(0.0, 0.0)));
        member8.updateRecentActiveInfo(new RecentActiveInfo(LocalDateTime.now(), new Point(0.0, 0.0)));
        member9.updateRecentActiveInfo(new RecentActiveInfo(LocalDateTime.now(), new Point(0.0, 0.0)));
        memberRepository.saveAll(List.of(
                member1,
                member2,
                member3,
                member4,
                member5,
                member6,
                member7,
                member8,
                member9
        ));
    }

    @Test
    void test1() {
        // given
        RecommendQueryParam param = new RecommendQueryParam(
                100L,
                FEMALE,
                30,
                20,
                false,
                20,
                false,
                0.0,
                0.0,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findRecommended(param);

        // then
        assertThat(candidates).hasSize(2);
    }

    @Test
    void test2() {
        // given
        RecommendQueryParam param = new RecommendQueryParam(
                100L,
                FEMALE,
                29,
                20,
                false,
                20,
                false,
                0.0,
                0.0,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findRecommended(param);

        // then
        assertThat(candidates).hasSize(1);
    }

    @Test
    void test3() {
        // given
        RecommendQueryParam param = new RecommendQueryParam(
                100L,
                MALE,
                50,
                50,
                false,
                20,
                false,
                0.0,
                0.0,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findRecommended(param);

        // then
        assertThat(candidates).hasSize(0);
    }

    @Test
    void test4() {
        // given
        RecommendQueryParam param = new RecommendQueryParam(
                100L,
                FEMALE,
                50,
                50,
                false,
                20,
                false,
                0.0,
                0.0,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findRecommended(param);

        // then
        assertThat(candidates).hasSize(1);
    }

    @Test
    void test5() {
        // given
        RecommendQueryParam param = new RecommendQueryParam(
                100L,
                FEMALE,
                50,
                50,
                false,
                20,
                false,
                2.0,
                2.0,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findRecommended(param);

        // then
        assertThat(candidates).hasSize(0);
    }

    @Test
    void test6() {
        // given
        RecommendWithoutDistanceQueryParam param = new RecommendWithoutDistanceQueryParam(
                100L,
                FEMALE,
                100,
                0,
                false,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findRecommendedWithoutDistance(param);

        // then
        assertThat(candidates).hasSize(5);
    }

    @Test
    void test7() {
        // given
        RecommendByIdsQueryParam param = new RecommendByIdsQueryParam(
                100L,
                List.of(member1.getId(), member2.getId(), member3.getId()),
                100,
                true,
                10.0,
                10.0
        );

        // when
        List<Long> candidates = recommendQuery.findRecommendedByIdsIn(param);

        // then
        assertThat(candidates).hasSize(0);
    }

    @Test
    void test8() {
        // given
        RecommendByIdsQueryParam param = new RecommendByIdsQueryParam(
                100L,
                List.of(member1.getId(), member2.getId(), member3.getId()),
                100,
                true,
                0.0,
                0.0
        );

        // when
        List<Long> candidates = recommendQuery.findRecommendedByIdsIn(param);

        // then
        assertThat(candidates).hasSize(3);
    }
}
