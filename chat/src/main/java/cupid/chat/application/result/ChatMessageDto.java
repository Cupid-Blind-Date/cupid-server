package cupid.chat.application.result;

import cupid.chat.domain.ChatMessage;
import cupid.chat.domain.ChatMessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record ChatMessageDto(
        Long id,
        Long chatRoomId,
        Long senderId,
        Long targetId,
        String message,
        @Enumerated(EnumType.STRING)
        ChatMessageType chatMessageType,
        boolean read
) {
    public static ChatMessageDto from(ChatMessage chatMessage) {
        return new ChatMessageDto(
                chatMessage.getId(),
                chatMessage.getChatRoomId(),
                chatMessage.getSenderId(),
                chatMessage.getTargetId(),
                chatMessage.getMessage(),
                chatMessage.getChatMessageType(),
                chatMessage.isRead()
        );
    }
}
