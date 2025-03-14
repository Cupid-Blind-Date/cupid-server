package cupid.async.queuecapacity;

import static cupid.async.queuecapacity.QueueCapacityCheckConfig.QUEUE_CAPACITY_CHECK_TASK_EXECUTOR;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import cupid.async.event.TestDomainEventRepository;
import cupid.async.event.TestEventProducer;


@Slf4j
@RequiredArgsConstructor
@Service
public class QueueCapacityCheckEventListener {

    private final TestDomainEventRepository domainEventRepository;
    private final TestEventProducer eventPublisher;

    @Async(QUEUE_CAPACITY_CHECK_TASK_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(QueueCapacityCheckEvent domainEvent) {
        try {
            log.info("MaxQueueCapacityCheckEventListener.publishEvent(). id: {}", domainEvent.getRequestId());
            Thread.sleep(60 * 60 * 1000);  // 1시간 대기
            eventPublisher.publish(domainEvent);
            domainEvent.publishSuccess();
            domainEventRepository.save(domainEvent);
        } catch (Exception e) {
            log.error("Exception occurred during publish event. e: {}. message: {}", e.getClass(), e.getMessage());
            domainEvent.publishFail();
            domainEventRepository.save(domainEvent);
        }
    }
}
