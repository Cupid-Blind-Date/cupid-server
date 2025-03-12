package test.async.event;

import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestDomainEventPublishCallback implements BiConsumer<SendResult<String, Object>, Throwable> {

    private final TestDomainEventRepository domainEventRepository;

    @Override
    public void accept(SendResult<String, Object> result, Throwable throwable) {
        String value = (String) result.getProducerRecord().value();
        if (throwable == null) {
            log.info("(callback) Successfully produced topic. eventId: {}", value);
            TestDomainEvent event = domainEventRepository.getByUuid(value);
            event.publishSuccess();
            domainEventRepository.save(event);
            return;
        }
        log.error("(callback) Unexpected exception while produce topic to broker. "
                  + "eventId: {}, e: {}, message: {}",
                value, throwable.getClass(), throwable.getMessage());
        TestDomainEvent event = domainEventRepository.getByUuid(value);
        event.publishFail();
        domainEventRepository.save(event);
        log.info("Successfully failed the topic. eventId: {}", value);
    }
}
