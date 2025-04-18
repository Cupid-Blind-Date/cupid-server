package cupid.infra.kafka.consumer;

import static org.assertj.core.api.Assertions.assertThat;

import cupid.common.kafka.KafkaDomainEventMessage;
import cupid.common.kafka.consumer.KafkaIdempotencyFilter;
import cupid.common.kafka.consumer.KafkaMessageConsumeHistory;
import cupid.common.kafka.consumer.KafkaMessageProcessHistoryRepository;
import cupid.common.kafka.deadletter.DeadLetter;
import cupid.common.kafka.deadletter.DeadLetterRepository;
import cupid.support.ApplicationTest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayName("KafkaIdempotencyFilter 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class KafkaIdempotencyFilterTest extends ApplicationTest {

    @Autowired
    private KafkaIdempotencyFilter kafkaIdempotencyFilter;

    @Autowired
    private KafkaMessageProcessHistoryRepository kafkaMessageProcessHistoryRepository;

    @Autowired
    private DeadLetterRepository deadLetterRepository;

    @Test
    void 최초_메세지라면_처리한다() {
        // given
        ConsumerRecord record = new ConsumerRecord("topic", 1, 1L, "", new KafkaDomainEventMessage(1L, "uuid", 1L));

        // when
        boolean skip = kafkaIdempotencyFilter.filter(record);

        // then
        assertThat(skip).isFalse();
    }

    @Test
    void 멱등성을_보장하기_위해_중복_메세지는_처리하지_않는다() {
        // given
        kafkaMessageProcessHistoryRepository.save(new KafkaMessageConsumeHistory("topic", "uuid"));
        ConsumerRecord record = new ConsumerRecord("topic", 1, 1L, "", new KafkaDomainEventMessage(1L, "uuid", 1L));

        // when
        boolean skip = kafkaIdempotencyFilter.filter(record);

        // then
        assertThat(skip).isTrue();
    }

    @Test
    void 예외가_발생한_이력이_있는_중복_메세지라도_처리하지_않는다() {
        // given
        deadLetterRepository.save(new DeadLetter("uuid", "fail", false));
        kafkaMessageProcessHistoryRepository.save(new KafkaMessageConsumeHistory("topic", "uuid"));
        ConsumerRecord record = new ConsumerRecord("topic", 1, 1L, "", new KafkaDomainEventMessage(1L, "uuid", 1L));

        // when
        boolean skip = kafkaIdempotencyFilter.filter(record);

        // then
        assertThat(skip).isTrue();
    }
}
