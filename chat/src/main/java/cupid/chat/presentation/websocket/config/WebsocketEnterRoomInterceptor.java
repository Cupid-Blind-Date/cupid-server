package cupid.chat.presentation.websocket.config;

import static cupid.chat.presentation.websocket.config.WebSocketConfig.CLIENT_CHAT_SUBSCRIBE_PREFIX;

import cupid.chat.domain.ChatRoom;
import cupid.chat.domain.ChatRoomRepository;
import cupid.chat.exception.ChatExceptionCode;
import cupid.common.auth.TokenExceptionCode;
import cupid.common.exception.ApplicationException;
import cupid.common.exception.InternalServerExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebsocketEnterRoomInterceptor implements ChannelInterceptor {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("Try to check authority to enter room Websocket Request");
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (headerAccessor == null) {
            log.error("Not found header accessor");
            throw new ApplicationException(InternalServerExceptionCode.UNKNOWN_EXCEPTION);
        }
        // WebSocket SUBSCRIBE 요청 처리 (채팅방 참가 권한 확인)
        if (headerAccessor.getCommand() == StompCommand.SUBSCRIBE) {
            authorizeRoomAccess(headerAccessor);
        }
        return message;
    }

    private void authorizeRoomAccess(StompHeaderAccessor headerAccessor) {
        String destination = headerAccessor.getDestination();
        log.info("Websocket subscribe destination is {}", destination);
        if (destination == null || !destination.startsWith(CLIENT_CHAT_SUBSCRIBE_PREFIX)) {
            log.info("no need to authorization room.");
            return;
        }

        Long roomId = getChatRoomId(destination);
        Long memberId = (Long) headerAccessor.getSessionAttributes().get("memberId");
        if (memberId == null) {
            throw new ApplicationException(TokenExceptionCode.UNAUTHORIZED);
        }
        ChatRoom room = chatRoomRepository.getById(roomId);
        room.validateParticipants(memberId);
        log.info("User {} authorized for chat room {}", memberId, roomId);
    }

    private Long getChatRoomId(String destination) {
        String roomIdStr = destination.replace(CLIENT_CHAT_SUBSCRIBE_PREFIX, "");
        try {
            return Long.parseLong(roomIdStr);
        } catch (NumberFormatException e) {
            throw new ApplicationException(ChatExceptionCode.INVALID_CHAT_ROOM_ID);
        }
    }
}
