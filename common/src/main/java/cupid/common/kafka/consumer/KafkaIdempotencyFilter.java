package cupid.common.kafka.consumer;

import cupid.common.kafka.KafkaDomainEventMessage;
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

    /**
     * true 를 반환할 경우 처리하지 않는다.
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
        if (optHistory.isPresent()) {
            // 중복 consume 인 경우, 처리하지 않는다.
            log.info("Skip duplicated record. topic: {}, uuid: {}, offset: {}", topic, uuid, offset);
            return true;
        }
        // 최초 consume 인 경우, 이력을 저장하고 처리한다.
        log.info("Try to attempt consume first time. topic: {}, uuid: {}, offset: {}", topic, uuid, offset);
        KafkaMessageConsumeHistory history = KafkaMessageConsumeHistory.create(topic, data);
        kafkaMessageProcessHistoryRepository.save(history);
        return false;
    }
}
