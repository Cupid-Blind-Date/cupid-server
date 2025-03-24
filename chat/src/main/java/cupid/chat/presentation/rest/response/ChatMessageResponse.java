package cupid.chat.presentation.rest.response;

import cupid.chat.application.result.ChatMessageDto;
import cupid.chat.domain.ChatMessageType;

public record ChatMessageResponse(
        Long id,
        Long chatRoomId,
        Long senderId,
        Long targetId,
        String message,
        ChatMessageType chatMessageType,
        boolean read
) {
    public static ChatMessageResponse from(ChatMessageDto chatMessageDto) {
        return new ChatMessageResponse(
                chatMessageDto.id(),
                chatMessageDto.chatRoomId(),
                chatMessageDto.senderId(),
                chatMessageDto.targetId(),
                chatMessageDto.message(),
                chatMessageDto.chatMessageType(),
                chatMessageDto.read()
        );
    }
}
