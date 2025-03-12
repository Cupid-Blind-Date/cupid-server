package test.async.singlethread;

import static test.async.singlethread.SingleThreadAsyncConfig.SINGLE_THREAD_ASYNC_TASK_EXECUTOR;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import test.async.event.TestDomainEventRepository;
import test.async.event.TestEventPublisher;

@Slf4j
@RequiredArgsConstructor
@Service
public class SingleThreadEventPublisherListener {

    private final TestDomainEventRepository domainEventRepository;
    private final TestEventPublisher eventPublisher;

    @Async(SINGLE_THREAD_ASYNC_TASK_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(SingleThreadBlockEvent domainEvent) {
        try {
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
