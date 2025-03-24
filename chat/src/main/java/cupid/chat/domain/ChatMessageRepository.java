package cupid.chat.domain;

import static cupid.chat.exception.ChatExceptionCode.NOT_COUNT_CHAT_MESSAGE;

import cupid.common.exception.ApplicationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    default ChatMessage getById(Long id) {
        return findById(id).orElseThrow(() -> {
            throw new ApplicationException(NOT_COUNT_CHAT_MESSAGE);
        });
    }

    @Query("""
            SELECT m FROM ChatMessage m
            WHERE m.chatRoomId = :chatRoomId
            ORDER BY m.id DESC
            """)
    Page<ChatMessage> findAllByRoomAndTarget(Long chatRoomId, Pageable pageable);

    @Query("""
            SELECT m FROM ChatMessage m
            WHERE m.chatRoomId = :chatRoomId
                AND m.id < :lastReadId
            ORDER BY m.id DESC
            """)
    Page<ChatMessage> findAllByRoomAndTargetAndIdBefore(Long chatRoomId, Long lastReadId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("""
            UPDATE ChatMessage m
            SET m.read = TRUE
            WHERE m.chatRoomId = :chatRoomId
                AND m.targetId = :memberId
            """)
    void updateReadAllMessage(Long chatRoomId, Long memberId);
}
