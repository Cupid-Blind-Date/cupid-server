package cupid.kafka.consumer;

import static cupid.chat.presentation.websocket.config.WebSocketConfig.CLIENT_CHAT_SUBSCRIBE_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.chat.application.ChatProducer;
import cupid.chat.client.CoupleClient;
import cupid.chat.client.response.GetCoupleResponse;
import cupid.chat.domain.ChatMessage;
import cupid.chat.domain.ChatMessageType;
import cupid.chat.domain.ChatRoomRepository;
import cupid.chat.presentation.websocket.ChatTopicMessage;
import cupid.common.event.CoupleMatchEvent;
import cupid.common.event.publisher.DomainEventPublisher;
import cupid.support.ApplicationWithKafkaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DisplayName("KafkaConsumer 통합 테스트")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class KafkaConsumerIntegrationTest extends ApplicationWithKafkaTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @MockitoBean
    private CoupleClient client;

    @MockitoBean
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatProducer chatProducer;

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

    @Test
    void 메세지_전송_메세지를_받아_채팅방에_전달한다() {
        // when
        ChatMessage chatMessage = new ChatMessage(
                1L,
                1L,
                1L,
                "mesasge",
                ChatMessageType.TEXT
        );
        chatProducer.produce(chatMessage);
        ChatTopicMessage message = ChatTopicMessage.from(chatMessage);
        waitingConsumeTopicSync(ChatProducer.CHAT_TOPIC);

        // then
        verify(messagingTemplate, times(1))
                .convertAndSend(CLIENT_CHAT_SUBSCRIBE_PREFIX + 1L, message);
    }
}
