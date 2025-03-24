package cupid.chat.presentation.websocket;


import cupid.chat.application.ChatMessageService;
import cupid.chat.domain.ChatMessage;
import cupid.chat.kafka.producer.ReadChatProducer;
import cupid.chat.kafka.producer.SendChatProducer;
import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.ReadChatChannel;
import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.SendChatChannel;
import cupid.chat.presentation.websocket.request.ChatMessageRequest;
import cupid.chat.presentation.websocket.request.ReadMessageRequest;
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
    private final SendChatProducer sendChatProducer;
    private final ReadChatProducer readChatProducer;

    // 채팅 메시지 전송 시
    // /pub/chat/{roomId} 로 메세지를 보낸다.
    @MessageMapping(SendChatChannel.PUB_MESSAGE_MAPPING_URI)
    public void sendMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessageRequest request,
            StompHeaderAccessor headerAccessor
    ) {
        Long memberId = StompHeaderAccessorUtils.getMemberId(headerAccessor);
        Long targetId = StompHeaderAccessorUtils.getTargetId(headerAccessor);
        log.info("Try to send message. roomId: {}, senderId: {}, targetId: {}", roomId, memberId, targetId);
        ChatMessage message = chatMessageService.saveMessage(request.toCommand(roomId, memberId, targetId));
        sendChatProducer.produce(message);
        log.info("Successfully produce chat message. messageId: {}", message.getId());
    }

    // 채팅 메시지 읽음 정보 전송 시
    // /pub/read-chat/{roomId} 로 메세지를 보낸다.
    @MessageMapping(ReadChatChannel.PUB_MESSAGE_MAPPING_URI)
    public void sendReadMessageInfo(
            @DestinationVariable Long roomId,
            @Payload ReadMessageRequest request,
            StompHeaderAccessor headerAccessor
    ) {
        Long targetId = StompHeaderAccessorUtils.getTargetId(headerAccessor);
        Long messageId = request.messageId();
        ChatMessage message = chatMessageService.read(messageId);
        log.info("Try to send read message info. roomId: {}, messageId: {}, targetId: {}", roomId, messageId, targetId);

        readChatProducer.produce(message);
        log.info("Successfully produce read message info. messageId: {}", message.getId());
    }
}
