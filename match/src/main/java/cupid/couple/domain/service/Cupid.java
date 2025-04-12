package cupid.couple.domain.service;

import cupid.common.exception.ApplicationException;
import cupid.couple.domain.Arrow;
import cupid.couple.domain.ArrowRepository;
import cupid.couple.domain.Couple;
import cupid.couple.domain.CoupleRepository;
import cupid.couple.event.CoupleMatchEvent;
import cupid.couple.exception.CoupleExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class Cupid {

    private final ArrowRepository arrowRepository;
    private final TransactionalCupid transactionalCupid;

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
            return transactionalCupid.match(arrow);
        } catch (ApplicationException e) {
            // 동시에 좋아요를 보내 발생하는 Couple 중복생성 오류, 해당 오류는 무시하면 됨
            log.warn("Duplicate Couple creation detected due to simultaneous likes. "
                     + "Ignoring this error. message: {}", e.getMessage());
            return MatchResult.FAIL;
        }
    }

    /**
     * match 로직을 Cupid 클래스에서 바로 진행하려는 경우 예외를 catch 한 뒤  return MatchResult.FAIL; 를 하는 과정에서
     * Transaction silently rolled back because it has been marked as rollback-only
     * 가 발생.
     * 즉 트랜잭션 내에서 오류가 발생하면 반드시 예외를 밖으로 던져줘야 함.
     */
    @RequiredArgsConstructor
    @Component
    static class TransactionalCupid {

        private final CoupleRepository coupleRepository;
        private final ApplicationEventPublisher eventPublisher;

        @Transactional
        public MatchResult match(Arrow arrow) {
            try {
                Couple couple = arrow.match();

                log.info("Try to create Couple. higherId={}, lowerId={}", couple.getHigherId(), couple.getLowerId());
                coupleRepository.save(couple);
                log.info("Successfully created Couple. id={}", couple.getId());

                eventPublisher.publishEvent(new CoupleMatchEvent(couple.getId()));
                return MatchResult.SUCCESS;
            } catch (DataIntegrityViolationException e) {
                throw new ApplicationException(CoupleExceptionCode.DUPLICATED_COUPLE);
            }
        }
    }
}
