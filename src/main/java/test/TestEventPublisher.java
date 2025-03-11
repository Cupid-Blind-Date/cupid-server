package test;

import cupid.evnet.DomainEvent;
import cupid.evnet.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestEventPublisher implements EventPublisher {

    @Override
    public void publish(DomainEvent domainEvent) {
        try {
            log.info("Try to publish event: {}", domainEvent.getId());
            Thread.sleep(50);
            log.info("Successfully publish event: {}", domainEvent.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
