package cupid.chat.application;

import static cupid.common.exception.InternalServerExceptionCode.UNKNOWN_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.chat.domain.ChatMessage;
import cupid.chat.presentation.websocket.ChatTopicMessage;
import cupid.common.exception.ApplicationException;
import cupid.common.exception.ExceptionCode;
import cupid.common.kafka.producer.KafkaProducer;
import cupid.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("ChatProducer 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ChatProducerTest extends UnitTest {

    @InjectMocks
    private ChatProducer chatProducer;

    @Mock
    private KafkaProducer<ChatTopicMessage> kafkaProducer;

    @Test
    void 채팅_메세지를_카프카로_전송한다() {
        // given
        ChatMessage message = sut.giveMeBuilder(ChatMessage.class)
                .sample();

        // when
        chatProducer.produce(message);

        // then
        verify(kafkaProducer, times(1))
                .produce(ChatProducer.CHAT_TOPIC, ChatTopicMessage.from(message));
    }

    @Test
    void 예외_발생() {
        // given
        ChatMessage message = sut.giveMeBuilder(ChatMessage.class)
                .sample();
        ChatTopicMessage from = ChatTopicMessage.from(message);
        willThrow(RuntimeException.class)
                .given(kafkaProducer)
                .produce(ChatProducer.CHAT_TOPIC, from);

        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class, () -> {
            chatProducer.produce(message);
        }).getCode();
        assertThat(code).isEqualTo(UNKNOWN_EXCEPTION);
    }
}
