package cupid.chat.presentation.websocket;


import cupid.chat.application.ChatMessageService;
import cupid.chat.application.ChatProducer;
import cupid.chat.domain.ChatMessage;
import cupid.chat.presentation.websocket.request.ChatMessageRequest;
import cupid.chat.presentation.websocket.utils.StompHeaderAccessorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatWebsocketController {

    private final ChatMessageService chatMessageService;
    private final ChatProducer chatProducer;

    // /pub/chat/{roomId} 로 메세지를 보낸다.
    @MessageMapping("/chat/{roomId}")
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessageRequest request,
            StompHeaderAccessor headerAccessor
    ) {
        Long memberId = StompHeaderAccessorUtils.getMemberId(headerAccessor);
        Long targetId = StompHeaderAccessorUtils.getTargetId(headerAccessor);
        log.info("Try to send message. roomId: {}, senderId: {}, targetId: {}", roomId, memberId, targetId);
        ChatMessage message = chatMessageService.saveMessage(request.toCommand(roomId, memberId, targetId));
        chatProducer.produce(message);
        log.info("Successfully produce chat message. chatId: {}", message.getId());
    }
}
