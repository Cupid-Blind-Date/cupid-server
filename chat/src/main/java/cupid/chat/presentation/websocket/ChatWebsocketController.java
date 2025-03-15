package cupid.chat.presentation.websocket;


import static cupid.chat.presentation.websocket.config.WebSocketConfig.CLIENT_DESTINATION_PREFIX;
import static cupid.kafka.consumer.ChatKafkaConsumerConfig.CHAT_CONTAINER_FACTORY;

import cupid.chat.application.ChatMessageService;
import cupid.chat.application.ChatProducer;
import cupid.chat.domain.ChatMessage;
import cupid.chat.presentation.websocket.request.ChatMessageRequest;
import cupid.common.auth.TokenExceptionCode;
import cupid.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatWebsocketController {

    private final ChatMessageService chatMessageService;
    private final ChatProducer chatProducer;
    private final SimpMessagingTemplate messagingTemplate;

    // /sub/chat/{roomId} 를 구독하고,
    // /pub/chat/{roomId} 로 메세지를 보낸다.
    @MessageMapping("/chat/{roomId}")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessageRequest request,
            StompHeaderAccessor headerAccessor
    ) {
        log.info("Try to send message. roomId: {}", roomId);
        Long memberId = (Long) headerAccessor.getSessionAttributes().get("memberId");
        if (memberId == null) {
            throw new ApplicationException(TokenExceptionCode.REQUIRED_TOKEN);
        }
        ChatMessage message = chatMessageService.saveMessage(request.toCommand(roomId, memberId));
        chatProducer.produce(message);
        log.info("Successfully produce chat message. chatId: {}", message.getId());
    }

    // /sub/chat/{roomId} 를 구독한 클라이언트에게 메세지를 보낸다.
    @KafkaListener(topics = ChatProducer.CHAT_TOPIC, containerFactory = CHAT_CONTAINER_FACTORY)
    public void consumeChatTopic(
            ChatTopicMessage message,
            Acknowledgment ack,
            @Header(KafkaHeaders.OFFSET) int offset
    ) {
        Long messageId = message.chatMessageId();
        log.info("Try to consume chat message topic. messageId:{}, offset: {}", messageId, offset);
        Long roomId = message.roomId();
        messagingTemplate.convertAndSend(CLIENT_DESTINATION_PREFIX + "/chat/" + roomId, message);
        ack.acknowledge();
        log.info("Successfully consume chat message topic. messageId:{}, offset: {}", messageId, offset);
    }
}
