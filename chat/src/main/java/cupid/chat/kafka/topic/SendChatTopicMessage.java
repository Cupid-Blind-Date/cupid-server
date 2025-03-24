package cupid.chat.kafka.topic;

import cupid.chat.domain.ChatMessage;
import cupid.chat.domain.ChatMessageType;
import java.time.LocalDateTime;

public record SendChatTopicMessage(
        Long chatMessageId,
        Long roomId,
        Long senderId,
        Long targetId,
        String message,
        ChatMessageType chatMessageType,
        LocalDateTime createdDate
) {
    public static SendChatTopicMessage from(ChatMessage chatMessage) {
        return new SendChatTopicMessage(
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
