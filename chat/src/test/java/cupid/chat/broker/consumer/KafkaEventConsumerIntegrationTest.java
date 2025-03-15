package cupid.chat.broker.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import cupid.chat.client.CoupleClient;
import cupid.chat.client.response.GetCoupleResponse;
import cupid.chat.domain.ChatRoomRepository;
import cupid.common.evnet.CoupleMatchEvent;
import cupid.common.evnet.publisher.DomainEventPublisher;
import cupid.support.ApplicationWithKafkaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DisplayName("KafkaEventConsumer 통합 테스트")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class KafkaEventConsumerIntegrationTest extends ApplicationWithKafkaTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @MockitoBean
    private CoupleClient client;

    @Test
    void 커플_매치_이벤트를_받아_채팅방을_생성한다() {
        // given
        given(client.getCoupleById(1L))
                .willReturn(new GetCoupleResponse(1L, 2L, 1L));

        // when
        domainEventPublisher.publishWithTx(new CoupleMatchEvent(1L));
        waitingConsumeTopicSync(CoupleMatchEvent.COUPLE_MATCH_EVENT_TOPIC);

        // then
        assertThat(chatRoomRepository.findAll()).hasSize(1);
    }
}
