package test.async.virtualthread;

import static test.async.virtualthread.VirtualThreadAsyncConfig.VIRTUAL_THREAD_ASYNC_TASK_EXECUTOR;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import test.async.event.TestDomainEventRepository;
import test.async.event.TestEventProducer;

@Slf4j
@RequiredArgsConstructor
@Service
public class VirtualThreadBlockEventProducerListener {

    private final TestDomainEventRepository testDomainEventRepository;
    private final TestEventProducer eventPublisher;

    @Async(VIRTUAL_THREAD_ASYNC_TASK_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(VirtualThreadBlockEvent domainEvent) {
        try {
            log.info("Consume event");
            eventPublisher.publish(domainEvent);
            domainEvent.publishSuccess();
            testDomainEventRepository.save(domainEvent);
            log.info("Successfully produce topic");
        } catch (Exception e) {
            log.error("Exception occurred during publish event. e: {}. message: {}", e.getClass(), e.getMessage());
            domainEvent.publishFail();
            testDomainEventRepository.save(domainEvent);
        }
    }
}
