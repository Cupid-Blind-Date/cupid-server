package cupid.evnet.publisher;


import static cupid.common.exception.InternalServerExceptionCode.UNKNOWN_EXCEPTION;

import cupid.common.exception.ApplicationException;
import cupid.evnet.DomainEvent;
import cupid.evnet.DomainEventRepository;
import cupid.infra.kafka.KafkaDomainEventMessage;
import cupid.infra.kafka.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaProducer<KafkaDomainEventMessage> kafkaProducer;
    private final DomainEventRepository domainEventRepository;

    @Override
    public void publish(DomainEvent domainEvent) {
        String topic = domainEvent.getTopic();
        Long eventId = domainEvent.getId();
        try {
            KafkaDomainEventMessage message = KafkaDomainEventMessage.from(domainEvent);
            log.info("Try to publish kafka topic. topic: {}, eventId: {}", topic, eventId);
            kafkaProducer.produce(topic, message);
            log.info("Successfully publish kafka topic. topic: {}, eventId: {}", topic, eventId);
            domainEvent.publishSuccess();
            domainEventRepository.save(domainEvent);
            log.info("Successfully update domainEvent state to publish success. id: {}", domainEvent.getId());
        } catch (Exception e) {
            log.error("Unexpected exception while publish kafka topic: {}, message: {}", topic, e.getMessage(), e);
            domainEvent.publishFail(e);
            domainEventRepository.save(domainEvent);
            log.info("Successfully update domainEvent state to publish fail. id: {}", domainEvent.getId());
            throw new ApplicationException(UNKNOWN_EXCEPTION);
        }
    }
}
