package cupid.chat.presentation.websocket;

import cupid.chat.domain.ChatMessage;
import cupid.chat.domain.ChatMessageType;
import java.time.LocalDateTime;

public record ChatTopicMessage(
        Long chatMessageId,
        Long roomId,
        Long senderId,
        Long targetId,
        String message,
        ChatMessageType chatMessageType,
        LocalDateTime createdDate
) {
    public static ChatTopicMessage from(ChatMessage chatMessage) {
        return new ChatTopicMessage(
                chatMessage.getId(),
                chatMessage.getChatRoomId(),
                chatMessage.getSenderId(),
                chatMessage.getTargetId(),
                chatMessage.getMessage(),
                chatMessage.getChatMessageType(),
                chatMessage.getCreatedDate()
        );
    }
}
