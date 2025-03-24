package cupid.chat.kafka.topic;

import cupid.chat.domain.ChatMessage;

public record ReadChatTopicMessage(
        Long chatMessageId,
        Long roomId
) {
    public static ReadChatTopicMessage from(ChatMessage chatMessage) {
        return new ReadChatTopicMessage(
                chatMessage.getId(),
                chatMessage.getChatRoomId()
        );
    }
}
