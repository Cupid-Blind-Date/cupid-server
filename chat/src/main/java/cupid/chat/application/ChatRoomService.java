package cupid.chat.application;

import cupid.chat.domain.ChatRoom;
import cupid.chat.domain.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Long createChatRoom(Long higherId, Long lowerId) {
        log.info("Try to create chat room. higherId: {}, lowerId: {}", higherId, lowerId);
        ChatRoom chatRoom = new ChatRoom(higherId, lowerId);
        ChatRoom save = chatRoomRepository.save(chatRoom);
        log.info("Successfully create chat room. roomId: {}, higherId:{}, lowerId: {}",
                save.getId(), higherId, lowerId);
        return save.getId();
    }
}
