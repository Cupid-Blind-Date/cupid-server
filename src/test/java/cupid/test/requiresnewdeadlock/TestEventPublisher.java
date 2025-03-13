package cupid.test.requiresnewdeadlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestEventPublisher {

    private final Logger log = LoggerFactory.getLogger(TestEventPublisher.class);

    private final ApplicationEventPublisher publisher;

    public TestEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Transactional
    public void publishEvent(TestEvent event) {
        log.info("call TestEventPublisher.publish(). event-id: {}", event.getId());
        // 2 초간 대기
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("wake up! event-id: {}", event.getId());
        publisher.publishEvent(event);
    }
}
