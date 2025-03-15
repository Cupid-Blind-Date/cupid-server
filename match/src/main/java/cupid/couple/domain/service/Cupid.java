package cupid.couple.domain.service;

import cupid.common.evnet.CoupleMatchEvent;
import cupid.couple.domain.Arrow;
import cupid.couple.domain.ArrowRepository;
import cupid.couple.domain.Couple;
import cupid.couple.domain.CoupleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class Cupid {

    private final ArrowRepository arrowRepository;
    private final CoupleRepository coupleRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 커플 매칭
     */
    public MatchResult match(Arrow arrow) {
        // 상대방이 나에게 보낸 좋아요 요청 조회
        if (arrowRepository.doesNotExistsTargetLikeArrow(arrow.getTargetId(), arrow.getSenderId())) {
            // 상대방이 나에 보낸 좋아요가 없으면 바로 종료
            return MatchResult.FAIL;
        }
        try {
            Couple couple = arrow.match();

            log.info("Try to create Couple. higherId={}, lowerId={}", couple.getHigherId(), couple.getLowerId());
            coupleRepository.save(couple);
            log.info("Successfully created Couple. id={}", couple.getId());

            eventPublisher.publishEvent(new CoupleMatchEvent(couple.getId()));
            return MatchResult.SUCCESS;
        } catch (DataIntegrityViolationException e) {
            // 동시에 좋아요를 보내 발생하는 Couple 중복생성 오류, 해당 오류는 무시하면 됨
            log.warn("Duplicate Couple creation detected due to simultaneous likes. "
                     + "Ignoring this error. message: {}", e.getMessage());
            return MatchResult.FAIL;
        }
    }
}
