package cupid.common.event.producer;

import static cupid.common.event.producer.EventAsyncTaskExecutorConfig.EVENT_ASYNC_TASK_EXECUTOR;

import cupid.common.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventProducerListener {

    private final EventProducer eventProducer;

    // 트랜잭션 커밋 후, 메세지 브로커에 메시지 전송
    @Async(EVENT_ASYNC_TASK_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(DomainEvent domainEvent) {
        Long eventId = domainEvent.getId();
        String topic = domainEvent.getTopic();
        log.info("Consume domain event. id: {}, topic: {}", eventId, topic);
        eventProducer.produce(domainEvent);
    }
}
