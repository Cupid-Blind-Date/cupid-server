package cupid.chat.kafka.consumer;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.chat.application.ChatRoomService;
import cupid.chat.client.CoupleClient;
import cupid.chat.client.response.GetCoupleResponse;
import cupid.common.kafka.KafkaDomainEventMessage;
import cupid.support.UnitTest;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.support.Acknowledgment;

@DisplayName("KafkaEventConsumer 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class KafkaEventConsumerTest extends UnitTest {

    @InjectMocks
    private KafkaEventConsumer kafkaEventConsumer;

    @Mock
    private ChatRoomService chatRoomService;

    @Mock
    private CoupleClient client;

    @Test
    void 커플_매치_이벤트를_받아_채팅방을_생성한다() {
        // given
        given(client.getCoupleById(1L))
                .willReturn(new GetCoupleResponse(1L, 2L, 1L));
        Acknowledgment ack = mock(Acknowledgment.class);

        // when
        kafkaEventConsumer.consumeCoupleMatchEvent(
                new KafkaDomainEventMessage(1L, UUID.randomUUID().toString(), 1L),
                ack,
                1
        );

        // then
        verify(ack, times(1)).acknowledge();
        verify(chatRoomService, times(1))
                .createChatRoom(2L, 1L);
    }
}
