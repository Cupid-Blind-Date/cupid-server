package cupid.couple.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.common.evnet.DomainEvent;
import cupid.couple.domain.Arrow;
import cupid.couple.domain.ArrowRepository;
import cupid.couple.domain.Couple;
import cupid.couple.domain.CoupleRepository;
import cupid.couple.domain.LikeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import cupid.support.UnitTest;

@DisplayName("Cupid 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class CupidTest extends UnitTest {

    @InjectMocks
    private Cupid cupid;

    @Mock
    private ArrowRepository arrowRepository;

    @Mock
    private CoupleRepository coupleRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private Long myId = 1L;
    private Long targetId = 2L;

    @Test
    void 상대방이_나에게_좋아요를_보낸_상태에서_나도_상대방을_좋아하면_커플을_탄생시킨다() {
        // given
        given(arrowRepository.doesNotExistsTargetLikeArrow(targetId, myId))
                .willReturn(false);
        Arrow arrow = new Arrow(myId, targetId, LikeType.LIKE);

        // when
        MatchResult result = cupid.match(arrow);

        // then
        assertThat(result).isEqualTo(MatchResult.SUCCESS);
        verify(applicationEventPublisher, times(1))
                .publishEvent(any(DomainEvent.class));
    }

    @Test
    void 상대방이_나에게_좋아요를_보내지_않았다면_커플을_탄생시키지_않는다() {
        // given
        given(arrowRepository.doesNotExistsTargetLikeArrow(targetId, myId))
                .willReturn(true);
        Arrow arrow = new Arrow(myId, targetId, LikeType.LIKE);

        // when
        MatchResult result = cupid.match(arrow);

        // then
        assertThat(result).isEqualTo(MatchResult.FAIL);
    }

    @Test
    void 상대방이_나에게_좋아요를_보낸_상태이나_이미_상대방과_내가_커플인_경우_로깅만_하고_별도의_처리는_하지_않는다() {
        // given
        given(arrowRepository.doesNotExistsTargetLikeArrow(targetId, myId))
                .willReturn(false);
        given(coupleRepository.save(any(Couple.class)))
                .willThrow(new DataIntegrityViolationException(""));
        Arrow arrow = new Arrow(myId, targetId, LikeType.LIKE);

        // when
        MatchResult result = cupid.match(arrow);

        // then
        assertThat(result).isEqualTo(MatchResult.FAIL);
    }
}
