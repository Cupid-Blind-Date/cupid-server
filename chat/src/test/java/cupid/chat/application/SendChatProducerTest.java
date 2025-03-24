package cupid.chat.application;

import static cupid.common.exception.InternalServerExceptionCode.UNKNOWN_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.chat.domain.ChatMessage;
import cupid.chat.kafka.producer.SendChatProducer;
import cupid.chat.kafka.topic.SendChatTopicMessage;
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

@DisplayName("SendChatProducer 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class SendChatProducerTest extends UnitTest {

    @InjectMocks
    private SendChatProducer sendChatProducer;

    @Mock
    private KafkaProducer<SendChatTopicMessage> kafkaProducer;

    @Test
    void 채팅_메세지를_카프카로_전송한다() {
        // given
        ChatMessage message = sut.giveMeBuilder(ChatMessage.class)
                .sample();

        // when
        sendChatProducer.produce(message);

        // then
        verify(kafkaProducer, times(1))
                .produce(SendChatProducer.SEND_CHAT_TOPIC, SendChatTopicMessage.from(message));
    }

    @Test
    void 예외_발생() {
        // given
        ChatMessage message = sut.giveMeBuilder(ChatMessage.class)
                .sample();
        SendChatTopicMessage from = SendChatTopicMessage.from(message);
        willThrow(RuntimeException.class)
                .given(kafkaProducer)
                .produce(SendChatProducer.SEND_CHAT_TOPIC, from);

        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class, () -> {
            sendChatProducer.produce(message);
        }).getCode();
        assertThat(code).isEqualTo(UNKNOWN_EXCEPTION);
    }
}
