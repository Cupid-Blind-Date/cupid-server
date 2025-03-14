package cupid.infra.kafka.deadletter;

import static org.assertj.core.api.Assertions.assertThat;

import cupid.common.kafka.KafkaDomainEventMessage;
import cupid.common.kafka.deadletter.DeadLetterRecorder;
import cupid.common.kafka.deadletter.DeadLetterRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cupid.support.ApplicationTest;

@Transactional
@DisplayName("DeadLetterRecorder 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class DeadLetterRecorderTest extends ApplicationTest {

    @Autowired
    private DeadLetterRecorder deadLetterRecorder;

    @Autowired
    private DeadLetterRepository deadLetterRepository;

    @Test
    void 카프카_consumer_과정에서_예외_발생_시_dead_letter_를_저장한다() {
        // given
        ConsumerRecord record = new ConsumerRecord("topic", 1, 1L, "", new KafkaDomainEventMessage(1L, "uuid", 1L));

        // when
        deadLetterRecorder.accept(record, new RuntimeException("Exception"));

        // then
        assertThat(deadLetterRepository.findByUuid("uuid")).isPresent();
    }
}
