package cupid.chat.application;

import static cupid.common.exception.InternalServerExceptionCode.UNKNOWN_EXCEPTION;

import cupid.chat.domain.ChatMessage;
import cupid.chat.domain.ChatMessageRepository;
import cupid.chat.presentation.websocket.ChatTopicMessage;
import cupid.common.exception.ApplicationException;
import cupid.common.kafka.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatProducer {

    public static final String CHAT_TOPIC = "CHAT";

    private final KafkaProducer<ChatTopicMessage> kafkaProducer;
    private final ChatMessageRepository chatMessageRepository;

    public void produce(ChatMessage chatMessage) {
        try {
            ChatTopicMessage message = ChatTopicMessage.from(chatMessage);
            log.info("Try to produce chat message topic. messageId: {}", chatMessage.getId());
            kafkaProducer.produce(CHAT_TOPIC, message);
            log.info("Successfully produce chat message topic. messageId: {}", chatMessage.getId());
            chatMessage.sendSuccess();
            chatMessageRepository.save(chatMessage);
            log.info("Successfully update chat message state to send success. messageId: {}", chatMessage.getId());
        } catch (Exception e) {
            log.error("Unexpected exception while produce chat message messageId: {}, exception message: {}",
                    chatMessage.getId(), e.getMessage(), e);
            throw new ApplicationException(UNKNOWN_EXCEPTION);
        }
    }
}
