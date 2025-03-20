package cupid.chat.application;

import cupid.chat.application.command.SendChatMessageCommand;
import cupid.chat.domain.ChatMessage;
import cupid.chat.domain.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(SendChatMessageCommand command) {
        log.info("Try to send chat message. chatRoomId: {}", command.chatRoomId());
        ChatMessage chatMessage = command.toChatMessage();
        chatMessageRepository.save(chatMessage);
        log.info("Successfully save chat message. chatId: {}", chatMessage.getId());
        return chatMessage;
    }
}
