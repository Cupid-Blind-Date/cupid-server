package cupid.chat.presentation.websocket.utils;

import cupid.chat.exception.ChatExceptionCode;
import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.ReadChatChannel;
import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.SendChatChannel;
import cupid.common.exception.ApplicationException;
import cupid.common.exception.InternalServerExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Slf4j
public final class StompHeaderAccessorUtils {

    public static StompHeaderAccessor getStompHeaderAccessor(Message<?> message) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (headerAccessor == null) {
            log.error("Not found header accessor");
            throw new ApplicationException(InternalServerExceptionCode.UNKNOWN_EXCEPTION);
        }
        return headerAccessor;
    }

    public static Long putMemberId(StompHeaderAccessor headerAccessor, Long memberId) {
        return (Long) headerAccessor.getSessionAttributes().put("memberId", memberId);
    }

    public static Long getMemberId(StompHeaderAccessor headerAccessor) {
        Object memberId = headerAccessor.getSessionAttributes().get("memberId");
        if (memberId == null) {
            throw new ApplicationException(ChatExceptionCode.UNAUTHENTICATED);
        }
        return (Long) memberId;
    }

    public static Long putRoomId(StompHeaderAccessor headerAccessor, Long roomId) {
        return (Long) headerAccessor.getSessionAttributes().put("roomId", roomId);
    }

    public static Long getRoomId(StompHeaderAccessor headerAccessor) {
        return (Long) headerAccessor.getSessionAttributes().get("roomId");
    }

    public static Long putTargetId(StompHeaderAccessor headerAccessor, Long targetId) {
        return (Long) headerAccessor.getSessionAttributes().put("targetId", targetId);
    }

    public static Long getTargetId(StompHeaderAccessor headerAccessor) {
        return (Long) headerAccessor.getSessionAttributes().get("targetId");
    }

    public static String getDestinationChatRoomId(StompHeaderAccessor headerAccessor) {
        String destination = headerAccessor.getDestination();
        return destination
                .replace(SendChatChannel.SUB, "")
                .replace(SendChatChannel.PUB, "")
                .replace(ReadChatChannel.SUB, "")
                .replace(ReadChatChannel.PUB, "");
    }
}
