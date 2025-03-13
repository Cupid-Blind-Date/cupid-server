package cupid.evnet.publisher;

import static cupid.evnet.EventState.PUBLISH_FAIL;
import static cupid.evnet.EventState.PUBLISH_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;

import cupid.common.exception.ApplicationException;
import cupid.evnet.DomainEventRepository;
import cupid.evnet.EventState;
import cupid.evnet.mock.TestEvent;
import cupid.infra.kafka.KafkaDomainEventMessage;
import cupid.infra.kafka.producer.KafkaProducer;
import cupid.support.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DisplayName("KafkaEventPublisher 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class KafkaEventPublisherTest extends ApplicationTest {

    @Autowired
    private KafkaEventPublisher kafkaEventPublisher;

    @MockitoBean
    private KafkaProducer<KafkaDomainEventMessage> kafkaProducer;

    @Autowired
    private DomainEventRepository domainEventRepository;

    @Test
    void 이벤트_발행에_성공하면_event_의_상태를_성공으로_저장() {
        // given
        TestEvent topic = new TestEvent(1L, "topic");

        // when
        kafkaEventPublisher.publish(topic);

        // then
        EventState state = domainEventRepository.getByUuid(topic.getUuid()).getState();
        assertThat(state).isEqualTo(PUBLISH_SUCCESS);
    }

    @Test
    void 이벤트_발행에_실패하면_event_의_상태를_성공으로_저장() {
        // given
        TestEvent topic = new TestEvent(1L, "topic");
        willThrow(RuntimeException.class)
                .given(kafkaProducer)
                .produce(any(), any());

        // when
        assertThatThrownBy(() -> {
            kafkaEventPublisher.publish(topic);
        }).isInstanceOf(ApplicationException.class);

        // then
        EventState state = domainEventRepository.getByUuid(topic.getUuid()).getState();
        assertThat(state).isEqualTo(PUBLISH_FAIL);
    }

}
