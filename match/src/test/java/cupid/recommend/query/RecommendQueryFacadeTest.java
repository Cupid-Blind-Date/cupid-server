package cupid.recommend.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import cupid.common.value.Point;
import cupid.filter.domain.AgeCondition;
import cupid.filter.domain.DistanceCondition;
import cupid.filter.domain.Filter;
import cupid.filter.domain.FilterRepository;
import cupid.filter.domain.GenderCondition;
import cupid.support.ApplicationTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@DisplayName("추천 쿼리 파사드 (RecommendQueryFacade) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RecommendQueryFacadeTest extends ApplicationTest {

    @Autowired
    private RecommendQueryFacade recommendQueryFacade;

    @MockitoBean
    private RecommendQuery recommendQuery;

    @MockitoBean
    private FilterRepository filterRepository;

    private final Filter filter = new Filter(
            1L,
            new AgeCondition(100, 10, true),
            new DistanceCondition(100, true),
            GenderCondition.ONLY_MALE
    );

    @Test
    void 대상_추천_시_거리조건을_가지고_조회() {
        // given
        given(filterRepository.getByMemberId(1L)).willReturn(filter);
        given(recommendQuery.findRecommended(any())).willReturn(List.of(2L, 3L));

        // when
        List<Long> result = recommendQueryFacade.findRecommends(1L, new Point(1.1, 2.2), 10);

        // then
        assertThat(result).containsExactly(2L, 3L);
    }

    @Test
    void 대상_추천_시_거리조건_없이_조회() {
        // given
        given(filterRepository.getByMemberId(1L)).willReturn(filter);
        given(recommendQuery.findRecommendedWithoutDistance(any())).willReturn(List.of(2L, 3L));

        // when
        List<Long> result = recommendQueryFacade.findRecommends(1L, new Point(), 10);

        // then
        assertThat(result).containsExactly(2L, 3L);
    }

    @Test
    void 주어진_id_중_거리조건에_부합하는_회원_조회() {
        // given
        given(filterRepository.getByMemberId(1L)).willReturn(filter);
        given(recommendQuery.findRecommendedByIdsIn(any())).willReturn(List.of(2L));

        // when
        List<Long> result = recommendQueryFacade.findRecommendedByIdsIn(1L, List.of(2L, 3L), new Point(1.1, 2.2));

        // then
        assertThat(result).containsExactly(2L);
    }
}
