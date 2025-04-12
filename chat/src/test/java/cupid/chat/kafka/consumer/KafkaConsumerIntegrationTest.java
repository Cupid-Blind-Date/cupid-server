package cupid.chat.kafka.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.chat.client.CoupleClient;
import cupid.chat.client.response.GetCoupleResponse;
import cupid.chat.domain.ChatMessage;
import cupid.chat.domain.ChatMessageType;
import cupid.chat.domain.ChatRoomRepository;
import cupid.chat.kafka.producer.SendChatProducer;
import cupid.chat.kafka.topic.SendChatTopicMessage;
import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.SendChatChannel;
import cupid.common.kafka.KafkaDomainEventMessage;
import cupid.common.kafka.producer.KafkaProducer;
import cupid.common.kafka.topic.KafkaTopic;
import cupid.support.ApplicationWithKafkaTest;
import java.util.UUID;
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
    private KafkaProducer<KafkaDomainEventMessage> kafkaProducer;

    @MockitoBean
    private CoupleClient coupleClient;

    @MockitoBean
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SendChatProducer sendChatProducer;

    @Test
    void 커플_매치_이벤트를_받아_채팅방을_생성한다() {
        // given
        given(coupleClient.getCoupleById(1L))
                .willReturn(new GetCoupleResponse(1L, 2L, 1L));

        // when
        kafkaProducer.produce(KafkaTopic.COUPLE_MATCH_EVENT_TOPIC,
                new KafkaDomainEventMessage(1L, UUID.randomUUID().toString(), 1L));
        waitingConsumeTopicSync(KafkaTopic.COUPLE_MATCH_EVENT_TOPIC);

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
        sendChatProducer.produce(chatMessage);
        SendChatTopicMessage message = SendChatTopicMessage.from(chatMessage);
        waitingConsumeTopicSync(SendChatProducer.SEND_CHAT_TOPIC);

        // then
        verify(messagingTemplate, times(1))
                .convertAndSend(SendChatChannel.SUB + 1L, message);
    }
}
