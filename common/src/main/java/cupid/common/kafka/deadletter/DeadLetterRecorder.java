package cupid.common.kafka.deadletter;

import cupid.common.kafka.KafkaDomainEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeadLetterRecorder implements ConsumerRecordRecoverer {

    private final DeadLetterRepository deadLetterRepository;

    /*
     * 해당 레코드 처리 시 예외가 발생하였을 때,
     * 재처리에도 실패한 경우 DeadLetter 에 기록한다.
     */
    @Override
    public void accept(
            ConsumerRecord<?, ?> consumerRecord,
            Exception e
    ) {
        KafkaDomainEventMessage message = (KafkaDomainEventMessage) consumerRecord.value();
        String topic = consumerRecord.topic();
        long offset = consumerRecord.offset();
        String uuid = message.uuid();
        log.info("Record deadLetter. topic: {}, uuid: {}, offset: {}. e: {}, message: {}, cause: {}",
                topic, uuid, offset, e.getClass(), e.getMessage(), e.getCause());
        DeadLetter deadLetter = DeadLetter.create(
                message,
                e.getCause() == null ? e.getMessage() : e.getCause().getMessage()
        );
        deadLetterRepository.save(deadLetter);
        log.info("Successfully saved deadLetter. deadLetterId: {}. uuid: {}", deadLetter.getId(), uuid);
    }
}
