package cupid.chat.presentation.websocket.config;

import static cupid.chat.exception.ChatExceptionCode.NO_AUTHORITY_FOR_CHAT_ROOM;
import static cupid.chat.presentation.websocket.utils.StompHeaderAccessorUtils.getDestinationChatRoomId;
import static cupid.chat.presentation.websocket.utils.StompHeaderAccessorUtils.getRoomId;

import cupid.chat.exception.ChatExceptionCode;
import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.ReadChatChannel;
import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.SendChatChannel;
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
public class WebsocketValidateRoomInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("Try to validate room Websocket Request");
        StompHeaderAccessor headerAccessor = StompHeaderAccessorUtils.getStompHeaderAccessor(message);
        StompCommand command = headerAccessor.getCommand();
        // pub/sub 하는 요청이 아닌 경우
        if (command != StompCommand.SEND && command != StompCommand.SUBSCRIBE) {
            log.info("No need to validate room Websocket Request");
            return message;
        }

        // 채팅방에 pub/sub 하는 요청인 경우
        String destination = headerAccessor.getDestination();
        log.info("Websocket subscribe destination is {}", destination);
        if (isSubscribeOrPublishChatRoomDestination(destination)) {
            validateRoomId(headerAccessor);
            return message;
        }

        // 채팅방에 대한 pub/sub 이 아닌 경우 (ex - 예외 채널 구독)
        log.info("No need to validate room Websocket Request");
        return message;
    }

    private boolean isSubscribeOrPublishChatRoomDestination(String destination) {
        if (destination == null) {
            return false;
        }
        return SendChatChannel.accept(destination) || ReadChatChannel.accept(destination);
    }

    private void validateRoomId(StompHeaderAccessor headerAccessor) {
        log.info("Try to validate roomId");
        String destinationChatRoomId = getDestinationChatRoomId(headerAccessor);
        log.info("destinationChatRoomId is {}", destinationChatRoomId);

        Long destinationRoomId = parseLong(destinationChatRoomId);
        Long roomId = getRoomId(headerAccessor);
        if (!destinationRoomId.equals(roomId)) {
            log.error("Invalid chat request. connected roomId is {}, but pub/sub roomId is {}",
                    roomId, destinationRoomId);
            throw new ApplicationException(NO_AUTHORITY_FOR_CHAT_ROOM);
        }
        log.info("Successfully validate roomId. roomId: {}", roomId);
    }

    public static long parseLong(String roomIdStr) {
        try {
            return Long.parseLong(roomIdStr);
        } catch (NumberFormatException e) {
            throw new ApplicationException(ChatExceptionCode.INVALID_CHAT_ROOM_ID);
        }
    }
}
