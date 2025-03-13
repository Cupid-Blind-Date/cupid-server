package cupid.infra.kafka.consumer;

import static cupid.kafka.consumer.KafkaConsumerConfig.DOMAIN_EVENT_CONTAINER_FACTORY;
import static org.assertj.core.api.Assertions.assertThat;

import cupid.kafka.KafkaDomainEventMessage;
import cupid.kafka.deadletter.DeadLetterRepository;
import cupid.kafka.producer.KafkaProducer;
import cupid.support.ApplicationWithKafkaTest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * 설정값을 변경한 경우 테스트한다.
 */
@Disabled
@DisplayName("카프카 컨슈머 설정 테스트 (KafkaConsumerConfig) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class KafkaConsumerConfigTest extends ApplicationWithKafkaTest {

    private final Long EXCEPTION_ID = Long.MAX_VALUE;

    @Autowired
    private KafkaProducer<KafkaDomainEventMessage> kafkaProducer;

    @Autowired
    private DeadLetterRepository deadLetterRepository;

    private static List<String> processedUuids = new ArrayList<>();

    @KafkaListener(topics = "KafkaConsumerConfigTest", containerFactory = DOMAIN_EVENT_CONTAINER_FACTORY)
    public void consumeCoupleMatchMessage(
            KafkaDomainEventMessage domainEventData,
            Acknowledgment ack,
            @Header(KafkaHeaders.OFFSET) int offset
    ) {
        processedUuids.add(domainEventData.uuid());
        if (EXCEPTION_ID.equals(domainEventData.targetDomainId())) {
            throw new RuntimeException("Exception!");
        } else {
            ack.acknowledge();
        }
    }

    @Test
    void 중복_요청인_경우_무시한다() throws InterruptedException {
        // given
        String uuid = UUID.randomUUID().toString();
        kafkaProducer.produce("KafkaConsumerConfigTest", new KafkaDomainEventMessage(
                1L,
                uuid,
                1L
        ));

        // when
        kafkaProducer.produce("KafkaConsumerConfigTest", new KafkaDomainEventMessage(
                1L,
                uuid,
                1L
        ));
        Thread.sleep(2000);

        // then
        assertThat(processedUuids.stream()
                .filter(it -> it.equals(uuid))
                .toList()).hasSize(1);
    }

    @Test
    void 예외가_발생하면_재처리하지_않고_dead_letter_에_저장한다() throws InterruptedException {
        // given
        String uuid = UUID.randomUUID().toString();

        // when
        kafkaProducer.produce("KafkaConsumerConfigTest", new KafkaDomainEventMessage(
                1L,
                uuid,
                EXCEPTION_ID
        ));
        Thread.sleep(2000);

        // then
        assertThat(deadLetterRepository.findByUuid(uuid)).isPresent();
    }
}
