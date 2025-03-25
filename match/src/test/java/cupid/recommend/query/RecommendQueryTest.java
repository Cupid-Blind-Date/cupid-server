package cupid.recommend.query;

import static org.assertj.core.api.Assertions.assertThat;

import cupid.member.domain.Gender;
import cupid.member.domain.Member;
import cupid.member.domain.MemberRepository;
import cupid.recommend.query.param.RecommendQueryParam;
import cupid.support.ApplicationTest;
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
    private Member member = new Member(UUID.randomUUID().toString(), "p", "1", 20, Gender.FEMALE);

    @BeforeEach
    void setUp() {
        memberRepository.saveAll(List.of(
                new Member(UUID.randomUUID().toString(), "p", "1", 10, Gender.FEMALE),
                member,
                new Member(UUID.randomUUID().toString(), "p", "1", 30, Gender.FEMALE),
                new Member(UUID.randomUUID().toString(), "p", "1", 40, Gender.FEMALE),
                new Member(UUID.randomUUID().toString(), "p", "1", 50, Gender.FEMALE),

                new Member(UUID.randomUUID().toString(), "p", "1", 10, Gender.MALE),
                new Member(UUID.randomUUID().toString(), "p", "1", 20, Gender.MALE),
                new Member(UUID.randomUUID().toString(), "p", "1", 30, Gender.MALE),
                new Member(UUID.randomUUID().toString(), "p", "1", 40, Gender.MALE)
        ));
    }

    @Test
    void test1() {
        // given
        RecommendQueryParam param = new RecommendQueryParam(
                1L,
                30,
                20,
                false,
                20,
                false,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findBothGenderRecommended(param);

        // then
        assertThat(candidates).hasSize(4);
    }

    @Test
    void test2() {
        // given
        RecommendQueryParam param = new RecommendQueryParam(
                member.getId(),
                30,
                20,
                false,
                20,
                false,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findBothGenderRecommended(param);

        // then
        assertThat(candidates).hasSize(3);
    }

    @Test
    void test3() {
        // given
        RecommendQueryParam param = new RecommendQueryParam(
                100L,
                30,
                20,
                false,
                20,
                false,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findMaleRecommended(param);

        // then
        assertThat(candidates).hasSize(2);
    }

    @Test
    void test4() {
        // given
        RecommendQueryParam param = new RecommendQueryParam(
                100L,
                29,
                20,
                false,
                20,
                false,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findFemaleRecommended(param);

        // then
        assertThat(candidates).hasSize(1);
    }

    @Test
    void test5() {
        // given
        RecommendQueryParam param = new RecommendQueryParam(
                100L,
                50,
                50,
                false,
                20,
                false,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findMaleRecommended(param);

        // then
        assertThat(candidates).hasSize(0);
    }

    @Test
    void test6() {
        // given
        RecommendQueryParam param = new RecommendQueryParam(
                100L,
                50,
                50,
                false,
                20,
                false,
                100
        );

        // when
        List<Long> candidates = recommendQuery.findFemaleRecommended(param);

        // then
        assertThat(candidates).hasSize(1);
    }
}
