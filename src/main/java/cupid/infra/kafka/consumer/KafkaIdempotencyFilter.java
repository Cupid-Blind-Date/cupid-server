package cupid.infra.kafka.consumer;

import cupid.infra.kafka.KafkaDomainEventMessage;
import cupid.infra.kafka.deadletter.DeadLetter;
import cupid.infra.kafka.deadletter.DeadLetterRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaIdempotencyFilter implements RecordFilterStrategy<String, KafkaDomainEventMessage> {

    private final KafkaMessageProcessHistoryRepository kafkaMessageProcessHistoryRepository;
    private final DeadLetterRepository deadLetterRepository;

    /**
     * false 를 반환할 경우 처리하지 않는다.
     */
    @Override
    public boolean filter(ConsumerRecord<String, KafkaDomainEventMessage> consumerRecord) {
        KafkaDomainEventMessage data = consumerRecord.value();
        String topic = consumerRecord.topic();
        String uuid = data.uuid();
        long offset = consumerRecord.offset();

        log.info("Try to filter already processed event record. topic: {}, uuid: {}, offset: {}",
                topic, uuid, offset);

        Optional<KafkaMessageConsumeHistory> optHistory = kafkaMessageProcessHistoryRepository.findByUuid(data.uuid());
        if (optHistory.isEmpty()) {
            log.info("Try to attempt consume first time. topic: {}, uuid: {}, offset: {}",
                    topic, uuid, offset);
            // 최초 consume 되었다면 이력을 저장하고, 처리한다.
            KafkaMessageConsumeHistory history = KafkaMessageConsumeHistory.create(topic, data);
            kafkaMessageProcessHistoryRepository.save(history);
            return false;
        }

        // 중복 consume 인 경우, 데드레터를 확인한다.
        log.info("Consumed duplicated record. check deadLetter. topic: {}, uuid: {}, offset: {}", topic, uuid, offset);

        Optional<DeadLetter> optDeadLetter = deadLetterRepository.findByUuid(uuid);
        if (optDeadLetter.isEmpty()) {
            // 데드레터가 없다면 중복이므로 처리하지 않는다.
            log.info("DeadLetter is not exists. Skipped duplicated record. topic: {}, uuid: {}, offset: {}",
                    topic, uuid, offset);
            return true;
        }

        DeadLetter deadLetter = optDeadLetter.get();
        if (deadLetter.isRecovered()) {
            // 데드레터가 있으나, recover 된(즉, 처리 완료된) 데드레터라면 해당 레코드는 처리하지 않는다.
            log.info("Skipped already processed record. topic: {}, uuid: {}, offset: {}",
                    topic, uuid, offset);
            return true;
        }

        // 데드레터가 있으나, recover 되지 않았다면, 해당 레코드는 다시 처리한다.
        log.info("Consume failed record. deadLetterId: {}. topic: {}, uuid: {}, offset: {}",
                deadLetter.getId(), topic, uuid, offset);
        return false;
    }
}
