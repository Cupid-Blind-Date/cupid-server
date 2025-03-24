package cupid.chat.kafka.producer;

import static cupid.common.exception.InternalServerExceptionCode.UNKNOWN_EXCEPTION;

import cupid.chat.domain.ChatMessage;
import cupid.chat.kafka.topic.ReadChatTopicMessage;
import cupid.common.exception.ApplicationException;
import cupid.common.kafka.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReadChatProducer {

    public static final String READ_CHAT_TOPIC = "READ_CHAT";

    private final KafkaProducer<ReadChatTopicMessage> kafkaProducer;

    public void produce(ChatMessage chatMessage) {
        try {
            ReadChatTopicMessage message = ReadChatTopicMessage.from(chatMessage);
            log.info("Try to produce read message topic. messageId: {}", chatMessage.getId());
            kafkaProducer.produce(READ_CHAT_TOPIC, message);
            log.info("Successfully produce read message info topic. messageId: {}", chatMessage.getId());
        } catch (Exception e) {
            log.error("Unexpected exception while produce chat message messageId: {}, exception message: {}",
                    chatMessage.getId(), e.getMessage(), e);
            throw new ApplicationException(UNKNOWN_EXCEPTION);
        }
    }
}
