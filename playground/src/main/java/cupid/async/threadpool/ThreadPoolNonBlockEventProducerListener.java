package cupid.async.threadpool;

import static cupid.async.threadpool.ThreadPoolAsyncConfig.THREAD_POOL_ASYNC_TASK_EXECUTOR;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import cupid.async.event.TestDomainEventPublishCallback;
import cupid.async.event.TestDomainEventRepository;
import cupid.async.event.TestEventProducer;

@Slf4j
@RequiredArgsConstructor
@Service
public class ThreadPoolNonBlockEventProducerListener {

    private final TestDomainEventPublishCallback callback;
    private final TestDomainEventRepository domainEventRepository;
    private final TestEventProducer eventPublisher;

    @Async(THREAD_POOL_ASYNC_TASK_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(ThreadPoolNonBlockEvent domainEvent) {
        try {
            eventPublisher.publish(domainEvent, callback);
        } catch (Exception e) {
            log.error("Exception occurred during publish event. e: {}. message: {}", e.getClass(), e.getMessage());
            domainEvent.publishFail();
            domainEventRepository.save(domainEvent);
        }
    }
}
