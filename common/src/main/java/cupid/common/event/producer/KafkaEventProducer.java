package cupid.common.event.producer;


import static cupid.common.exception.InternalServerExceptionCode.UNKNOWN_EXCEPTION;

import cupid.common.event.DomainEvent;
import cupid.common.event.DomainEventRepository;
import cupid.common.exception.ApplicationException;
import cupid.common.kafka.KafkaDomainEventMessage;
import cupid.common.kafka.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaEventProducer implements EventProducer {

    private final KafkaProducer<KafkaDomainEventMessage> kafkaProducer;
    private final DomainEventRepository domainEventRepository;

    @Override
    public void produce(DomainEvent domainEvent) {
        String topic = domainEvent.getTopic();
        Long eventId = domainEvent.getId();
        try {
            KafkaDomainEventMessage message = KafkaDomainEventMessage.from(domainEvent);
            log.info("Try to produce kafka topic. topic: {}, eventId: {}", topic, eventId);
            kafkaProducer.produce(topic, message);
            log.info("Successfully produce kafka topic. topic: {}, eventId: {}", topic, eventId);
            domainEvent.produceSuccess();
            domainEventRepository.save(domainEvent);
            log.info("Successfully update domainEvent state to produce success. id: {}", domainEvent.getId());
        } catch (Exception e) {
            log.error("Unexpected exception while produce kafka topic: {}, message: {}", topic, e.getMessage(), e);
            domainEvent.produceFail(e);
            domainEventRepository.save(domainEvent);
            log.info("Successfully update domainEvent state to produce fail. id: {}", domainEvent.getId());
            throw new ApplicationException(UNKNOWN_EXCEPTION);
        }
    }
}
