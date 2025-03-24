package cupid.chat.kafka.producer;

import static cupid.common.exception.InternalServerExceptionCode.UNKNOWN_EXCEPTION;

import cupid.chat.domain.ChatMessage;
import cupid.chat.kafka.topic.SendChatTopicMessage;
import cupid.common.exception.ApplicationException;
import cupid.common.kafka.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendChatProducer {

    public static final String SEND_CHAT_TOPIC = "CHAT";

    private final KafkaProducer<SendChatTopicMessage> kafkaProducer;

    public void produce(ChatMessage chatMessage) {
        try {
            SendChatTopicMessage message = SendChatTopicMessage.from(chatMessage);
            log.info("Try to produce chat message topic. messageId: {}", chatMessage.getId());
            kafkaProducer.produce("CHAT", message);
            log.info("Successfully produce chat message topic. messageId: {}", chatMessage.getId());
        } catch (Exception e) {
            log.error("Unexpected exception while produce chat message messageId: {}, exception message: {}",
                    chatMessage.getId(), e.getMessage(), e);
            throw new ApplicationException(UNKNOWN_EXCEPTION);
        }
    }
}
