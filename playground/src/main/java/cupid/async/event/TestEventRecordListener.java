package cupid.async.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestEventRecordListener {

    private final TestEventRecorder testEventRecorder;

    /**
     * 트랜잭션 커밋 전에, 발행된 이벤트를 DB 에 기록한다.
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void recordEvent(TestDomainEvent testDomainEvent) {
        testEventRecorder.record(testDomainEvent);
    }
}
