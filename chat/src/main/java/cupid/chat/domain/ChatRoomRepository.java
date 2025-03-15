package cupid.chat.domain;

import static cupid.chat.exception.ChatExceptionCode.NOT_COUNT_CHAT_ROOM;

import cupid.common.exception.ApplicationException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    default ChatRoom getById(Long id) {
        return findById(id).orElseThrow(() -> {
            throw new ApplicationException(NOT_COUNT_CHAT_ROOM);
        });
    }
}
