package cupid.chat.application;

import cupid.chat.application.command.SendChatMessageCommand;
import cupid.chat.application.result.ChatMessageDto;
import cupid.chat.domain.ChatMessage;
import cupid.chat.domain.ChatMessageRepository;
import cupid.chat.domain.ChatRoom;
import cupid.chat.domain.ChatRoomRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessage saveMessage(SendChatMessageCommand command) {
        log.info("Try to send chat message. chatRoomId: {}", command.chatRoomId());
        ChatMessage chatMessage = command.toChatMessage();
        chatMessageRepository.save(chatMessage);
        log.info("Successfully save chat message. chatId: {}", chatMessage.getId());
        return chatMessage;
    }

    /**
     * 채팅방 입장시 호출. 채팅방에 입장하면 모든 메시지를 읽음 처리한다.
     */
    public void readAllMessage(
            Long memberId,
            Long chatRoomId
    ) {
        ChatRoom room = chatRoomRepository.getById(chatRoomId);
        room.validateParticipants(memberId);
        chatMessageRepository.updateReadAllMessage(chatRoomId, memberId);
    }

    /**
     * 이전 채팅기록 조회
     */
    public Page<ChatMessageDto> findPreviousMessages(
            @Nullable Long lastReadId,  // cursor
            Long memberId,
            Long chatRoomId,
            int size
    ) {
        ChatRoom room = chatRoomRepository.getById(chatRoomId);
        room.validateParticipants(memberId);
        PageRequest pageRequest = PageRequest.of(0, size);
        if (lastReadId == null) {
            return chatMessageRepository.findAllByRoomAndTarget(chatRoomId, pageRequest)
                    .map(ChatMessageDto::from);
        }
        return chatMessageRepository.findAllByRoomAndTargetAndIdBefore(chatRoomId, lastReadId, pageRequest)
                .map(ChatMessageDto::from);
    }
}
