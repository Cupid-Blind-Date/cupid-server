package cupid.couple.application;

import static cupid.couple.domain.LikeType.DISLIKE;
import static cupid.couple.domain.LikeType.LIKE;
import static org.assertj.core.api.Assertions.assertThat;

import cupid.couple.application.command.LikeCommand;
import cupid.couple.domain.Arrow;
import cupid.couple.domain.ArrowRepository;
import cupid.couple.domain.Couple;
import cupid.couple.domain.CoupleRepository;
import cupid.couple.domain.service.MatchResult;
import cupid.support.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayName("CoupleService 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class CoupleServiceTest extends ApplicationTest {

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private CoupleRepository coupleRepository;

    @Autowired
    private ArrowRepository arrowRepository;

    private Long myId = 10L;
    private Long targetId = 5L;

    @Test
    void 좋아요_요청시_상대방도_나에게_좋아요를_보냈다면_커플_생성() {
        // given
        coupleService.like(new LikeCommand(targetId, myId));

        LikeCommand likeCommand = new LikeCommand(myId, targetId);

        // when
        MatchResult matchResult = coupleService.like(likeCommand);

        // then
        assertThat(arrowRepository.existsTargetArrow(myId, targetId, LIKE)).isTrue();
        assertThat(matchResult).isEqualTo(MatchResult.SUCCESS);
    }

    @Test
    void 커플_생성시_이벤트_발행() {
        // given
        coupleService.like(new LikeCommand(targetId, myId));

        LikeCommand likeCommand = new LikeCommand(myId, targetId);

        // when
        coupleService.like(likeCommand);

        // then
    }

    @Test
    void 좋아요_요청시_상대방이_나에게_좋아요를_보내지_않았다면_그대로_반환() {
        // given
        LikeCommand likeCommand = new LikeCommand(myId, targetId);

        // when
        MatchResult matchResult = coupleService.like(likeCommand);

        // then
        assertThat(arrowRepository.existsTargetArrow(myId, targetId, LIKE)).isTrue();
        assertThat(matchResult).isEqualTo(MatchResult.FAIL);
    }

    @Test
    void 좋아요_요청시_상대방이_나에게_싫어요를_보낸경우_그대로_반환() {
        // given
        coupleService.dislike(new LikeCommand(targetId, myId));
        LikeCommand likeCommand = new LikeCommand(myId, targetId);

        // when
        MatchResult matchResult = coupleService.like(likeCommand);

        // then
        assertThat(arrowRepository.existsTargetArrow(myId, targetId, LIKE)).isTrue();
        assertThat(matchResult).isEqualTo(MatchResult.FAIL);
    }

    @Test
    void 좋아요_요청시_이미_커플인_경우_그대로_반환() {
        // given
        coupleService.like(new LikeCommand(targetId, myId));
        Couple couple = new Arrow(targetId, myId, LIKE).match();
        coupleRepository.save(couple);

        LikeCommand likeCommand = new LikeCommand(myId, targetId);

        // when
        MatchResult matchResult = coupleService.like(likeCommand);

        // then
        assertThat(matchResult).isEqualTo(MatchResult.FAIL);
    }

    @Test
    void 상대방에게_싫어요를_보내는_경우_요청만_저장() {
        // given
        LikeCommand likeCommand = new LikeCommand(myId, targetId);

        // when
        coupleService.dislike(likeCommand);

        // then
        assertThat(arrowRepository.existsTargetArrow(myId, targetId, DISLIKE)).isTrue();
    }
}
