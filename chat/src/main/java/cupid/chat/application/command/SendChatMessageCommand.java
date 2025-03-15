package cupid.chat.application.command;

import cupid.chat.domain.ChatMessage;
import cupid.chat.domain.ChatMessageType;

public record SendChatMessageCommand(
        Long chatRoomId,
        Long senderId,
        String message,
        ChatMessageType chatMessageType
) {
    public ChatMessage toChatMessage(Long targetId) {
        return new ChatMessage(
                chatRoomId,
                senderId,
                targetId,
                message,
                chatMessageType
        );
    }
}
