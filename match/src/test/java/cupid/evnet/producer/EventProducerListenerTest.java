package cupid.evnet.producer;

import static cupid.common.evnet.EventState.PRODUCE_FAIL;
import static cupid.common.evnet.EventState.PRODUCE_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;

import cupid.common.evnet.DomainEventRepository;
import cupid.common.kafka.KafkaDomainEventMessage;
import cupid.common.kafka.producer.KafkaProducer;
import cupid.evnet.mock.TestEventService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import cupid.support.ApplicationTest;

@DisplayName("EventProducerListener 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class EventProducerListenerTest extends ApplicationTest {

    @MockitoBean
    private KafkaProducer<KafkaDomainEventMessage> kafkaProducer;

    @Autowired
    private TestEventService testEventService;

    @Autowired
    private DomainEventRepository domainEventRepository;

    @AfterEach
    void tearDown() {
        domainEventRepository.deleteAll();
    }

    @Disabled
    @Nested
    class 비동기_스레드_테스트 {

        // 비동기 스레드에서 작업되므로, 1초 멈춰야 테스트 성공
        @Test
        void 트랜잭션_커밋_후_이벤트를_메세지_브로커에_publish_하면_성공으로_기록() throws InterruptedException {
            // when
            testEventService.eventOccur(1L);
            Thread.sleep(2000);

            // then
            assertThat(domainEventRepository.findAll().get(0).getState()).isEqualTo(PRODUCE_SUCCESS);
        }

        // 비동기 스레드에서 작업되므로, 1초 멈춰야 테스트 성공
        @Test
        void 트랜잭션_커밋_후_이벤트를_메세지_브로커에_publish_에_실패하면_실패로_기록() throws InterruptedException {
            // given
            willThrow(RuntimeException.class)
                    .given(kafkaProducer)
                    .produce(any(), any());

            // when
            testEventService.eventOccur(1L);
            Thread.sleep(2000);

            // then
            assertThat(domainEventRepository.findAll().get(0).getState()).isEqualTo(PRODUCE_FAIL);
        }
    }
}
