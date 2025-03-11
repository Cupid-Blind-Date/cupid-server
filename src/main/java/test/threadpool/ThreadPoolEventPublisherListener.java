package test.threadpool;

import static test.threadpool.ThreadPoolAsyncConfig.THREAD_POOL_ASYNC_TASK_EXECUTOR;

import cupid.evnet.DomainEvent;
import cupid.evnet.DomainEventRepository;
import cupid.evnet.EventPublisher;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class ThreadPoolEventPublisherListener {

    private final DomainEventRepository domainEventRepository;
    private final EventPublisher eventPublisher;

    @Autowired
    @Qualifier(THREAD_POOL_ASYNC_TASK_EXECUTOR)
    private Executor executor;

    @Async(THREAD_POOL_ASYNC_TASK_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(DomainEvent domainEvent) {
        long approximatePendingTaskCount =
                ((ThreadPoolTaskExecutor) executor).getThreadPoolExecutor().getTaskCount()
                - ((ThreadPoolTaskExecutor) executor).getThreadPoolExecutor().getCompletedTaskCount();
        log.info("current approximatePendingTaskCount is: {}", approximatePendingTaskCount);

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
