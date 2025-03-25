package cupid.filter.application;

import static org.assertj.core.api.Assertions.assertThat;

import cupid.filter.application.command.FilterCreateCommand;
import cupid.filter.application.command.FilterUpdateCommand;
import cupid.filter.domain.Filter;
import cupid.filter.domain.FilterRepository;
import cupid.filter.domain.GenderCondition;
import cupid.support.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("FilterService 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class FilterServiceTest extends ApplicationTest {

    @Autowired
    private FilterService filterService;

    @Autowired
    private FilterRepository filterRepository;

    private Long memberId = 1L;

    @Test
    void 필터를_생성한다() {
        // given
        FilterCreateCommand condition = new FilterCreateCommand(
                memberId,
                30,
                20,
                false,
                10,
                true,
                GenderCondition.BOTH
        );

        // when
        Long id = filterService.createFilter(condition);

        // then
        Filter filter = filterRepository.findById(id).get();
        assertThat(filter.getMemberId()).isEqualTo(memberId);
        assertThat(filter.getAgeCondition().getMaxIncludeAge()).isEqualTo(30);
        assertThat(filter.getAgeCondition().getMinIncludeAge()).isEqualTo(20);
        assertThat(filter.getAgeCondition().isPermitExcessAge()).isFalse();
        assertThat(filter.getDistanceCondition().getMaxIncludeDistanceFromMe()).isEqualTo(10);
        assertThat(filter.getDistanceCondition().isPermitExcessDistance()).isTrue();
        assertThat(filter.getGenderCondition()).isEqualTo(GenderCondition.BOTH);
    }

    @Test
    void 필터를_업데이트한다() {
        FilterCreateCommand condition = new FilterCreateCommand(
                memberId,
                30,
                20,
                false,
                10,
                true,
                GenderCondition.BOTH
        );
        Long id = filterService.createFilter(condition);
        FilterUpdateCommand update = new FilterUpdateCommand(
                memberId,
                20,
                30,
                true,
                40,
                false,
                GenderCondition.ONLY_MALE
        );

        // when
        filterService.updateFilter(update);

        // then
        Filter filter = filterRepository.findById(id).get();
        assertThat(filter.getMemberId()).isEqualTo(memberId);
        assertThat(filter.getAgeCondition().getMaxIncludeAge()).isEqualTo(20);
        assertThat(filter.getAgeCondition().getMinIncludeAge()).isEqualTo(30);
        assertThat(filter.getAgeCondition().isPermitExcessAge()).isTrue();
        assertThat(filter.getDistanceCondition().getMaxIncludeDistanceFromMe()).isEqualTo(40);
        assertThat(filter.getDistanceCondition().isPermitExcessDistance()).isFalse();
        assertThat(filter.getGenderCondition()).isEqualTo(GenderCondition.ONLY_MALE);
    }

    @Test
    void 특정_회원이_필터를_생성하였는지_여부를_확인한다() {
        // given
        FilterCreateCommand condition = new FilterCreateCommand(
                memberId,
                30,
                20,
                false,
                10,
                true,
                GenderCondition.BOTH
        );
        filterService.createFilter(condition);

        // when
        boolean no = filterService.hasFilter(memberId + 1);
        boolean yes = filterService.hasFilter(memberId);

        // then
        assertThat(no).isFalse();
        assertThat(yes).isTrue();
    }
}
