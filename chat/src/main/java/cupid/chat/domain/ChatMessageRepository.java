package cupid.chat.domain;

import static cupid.chat.exception.ChatExceptionCode.NOT_COUNT_CHAT_MESSAGE;

import cupid.common.exception.ApplicationException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    default ChatMessage getById(Long id) {
        return findById(id).orElseThrow(() -> {
            throw new ApplicationException(NOT_COUNT_CHAT_MESSAGE);
        });
    }
}
