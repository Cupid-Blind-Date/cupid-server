package cupid.async.virtualthread;

import static cupid.async.virtualthread.VirtualThreadAsyncConfig.VIRTUAL_THREAD_ASYNC_TASK_EXECUTOR;

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
public class VirtualThreadNonBlockEventProducerListener {

    private final TestDomainEventPublishCallback callback;
    private final TestDomainEventRepository testDomainEventRepository;
    private final TestEventProducer eventPublisher;

    @Async(VIRTUAL_THREAD_ASYNC_TASK_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(VirtualThreadNonBlockEvent domainEvent) {
        try {
            eventPublisher.publish(domainEvent, callback);
        } catch (Exception e) {
            log.error("Exception occurred during publish event. e: {}. message: {}", e.getClass(), e.getMessage());
            domainEvent.publishFail();
            testDomainEventRepository.save(domainEvent);
        }
    }
}
