package test.async.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(TestDomainEvent domainEvent) {
        log.info("Try to publish event: {}, id: {}", domainEvent.getClass().getSimpleName(), domainEvent.getId());
        try {
            // 응답이 올때까지 block
            kafkaTemplate.send(domainEvent.getClass().getSimpleName(), domainEvent.getUuid()).get();
        } catch (Exception e) {
            log.error("Exception occur!. id: {}", domainEvent.getId());
            throw new RuntimeException(e);
        }
    }

    public void publish(TestDomainEvent domainEvent, TestDomainEventPublishCallback callback) {
        log.info("Try to publish event: {}, id: {}", domainEvent.getClass().getSimpleName(), domainEvent.getId());
        try {
            // non block
            kafkaTemplate.send(domainEvent.getClass().getSimpleName(), domainEvent.getUuid()).whenComplete(callback);
        } catch (Exception e) {
            log.error("Exception occur!. id: {}", domainEvent.getId());
            throw new RuntimeException(e);
        }
    }
}
