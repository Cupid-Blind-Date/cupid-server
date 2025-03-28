package cupid.recommend.query;

import static org.assertj.core.api.Assertions.assertThat;

import cupid.common.value.Point;
import cupid.member.domain.Gender;
import cupid.member.domain.Member;
import cupid.member.domain.MemberRepository;
import cupid.member.domain.RecentActiveInfo;
import cupid.recommend.query.param.RecommendByIdsQueryParam;
import cupid.recommend.query.param.RecommendQueryParam;
import cupid.recommend.query.param.RecommendWithoutDistanceQueryParam;
import cupid.support.ApplicationTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
    private Member member1 = new Member(UUID.randomUUID().toString(), "p", "1", 10, Gender.FEMALE);
    private Member member2 = new Member(UUID.randomUUID().toString(), "p", "1", 20, Gender.FEMALE);
    private Member member3 = new Member(UUID.randomUUID().toString(), "p", "1", 30, Gender.FEMALE);
    private Member member4 = new Member(UUID.randomUUID().toString(), "p", "1", 40, Gender.FEMALE);
    private Member member5 = new Member(UUID.randomUUID().toString(), "p", "1", 50, Gender.FEMALE);
    private Member member6 = new Member(UUID.randomUUID().toString(), "p", "1", 10, Gender.MALE);
    private Member member7 = new Member(UUID.randomUUID().toString(), "p", "1", 20, Gender.MALE);
    private Member member8 = new Member(UUID.randomUUID().toString(), "p", "1", 30, Gender.MALE);
    private Member member9 = new Member(UUID.randomUUID().toString(), "p", "1", 40, Gender.MALE);

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
                Gender.FEMALE,
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
                Gender.FEMALE,
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
                Gender.MALE,
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
                Gender.FEMALE,
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
                Gender.FEMALE,
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
                Gender.FEMALE,
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
