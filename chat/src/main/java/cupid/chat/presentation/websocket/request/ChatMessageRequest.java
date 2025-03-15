package cupid.chat.presentation.websocket.request;

import cupid.chat.application.command.SendChatMessageCommand;
import cupid.chat.domain.ChatMessageType;

public record ChatMessageRequest(
        Long targetId,
        String message,
        ChatMessageType chatMessageType
) {
    public SendChatMessageCommand toCommand(Long roomId, Long senderId) {
        return new SendChatMessageCommand(
                roomId,
                senderId,
                targetId,
                message,
                chatMessageType
        );
    }
}
