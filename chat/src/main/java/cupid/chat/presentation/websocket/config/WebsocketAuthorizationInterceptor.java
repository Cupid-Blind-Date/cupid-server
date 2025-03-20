package cupid.chat.presentation.websocket.config;

import static cupid.chat.presentation.websocket.utils.StompHeaderAccessorUtils.getMemberId;
import static cupid.chat.presentation.websocket.utils.StompHeaderAccessorUtils.putRoomId;
import static cupid.chat.presentation.websocket.utils.StompHeaderAccessorUtils.putTargetId;

import cupid.chat.domain.ChatRoom;
import cupid.chat.domain.ChatRoomRepository;
import cupid.chat.exception.ChatExceptionCode;
import cupid.chat.presentation.websocket.utils.StompHeaderAccessorUtils;
import cupid.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebsocketAuthorizationInterceptor implements ChannelInterceptor {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("Try to authorization Websocket Request");
        StompHeaderAccessor headerAccessor = StompHeaderAccessorUtils.getStompHeaderAccessor(message);

        // 최초 연결 시점인 경우
        StompCommand command = headerAccessor.getCommand();
        if (command == StompCommand.CONNECT) {
            authorization(headerAccessor);
            return message;
        }
        log.info("No need to authorization Websocket Request");
        return message;
    }

    private void authorization(StompHeaderAccessor headerAccessor) {
        log.info("Try to authorization");
        String roomIdStr = headerAccessor.getFirstNativeHeader("RoomId");
        if (roomIdStr == null) {
            throw new ApplicationException(ChatExceptionCode.NOT_FOUNT_CHAT_ROOM_ID);
        }
        long roomId = parseLong(roomIdStr);
        ChatRoom room = chatRoomRepository.getById(roomId);
        Long memberId = getMemberId(headerAccessor);
        room.validateParticipants(memberId);
        Long targetId = room.getTargetId(memberId);

        // 세션에 roomId, TargetId 저장
        putRoomId(headerAccessor, room.getId());
        putTargetId(headerAccessor, targetId);
        log.info("User {} authorized for chat room {}. targetId: {}", memberId, roomId, targetId);
    }

    public static long parseLong(String roomIdStr) {
        try {
            return Long.parseLong(roomIdStr);
        } catch (NumberFormatException e) {
            throw new ApplicationException(ChatExceptionCode.INVALID_CHAT_ROOM_ID);
        }
    }
}
