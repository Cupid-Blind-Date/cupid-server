package cupid.chat.domain;

import static cupid.chat.exception.ChatExceptionCode.NO_AUTHORITY_FOR_CHAT_ROOM;
import static cupid.common.SQLRestrictionClause.DELETED_AT_IS_NULL;

import cupid.common.domain.SoftDeletedDomain;
import cupid.common.exception.ApplicationException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction(DELETED_AT_IS_NULL)
@SQLDelete(sql = "UPDATE chat_room SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(
        name = "chat_room",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_chat_room_higher_id_lower_id", columnNames = {"higherId", "lowerId"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom extends SoftDeletedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long higherId;
    private Long lowerId;

    public ChatRoom(Long higherId, Long lowerId) {
        this.higherId = higherId;
        this.lowerId = lowerId;
    }

    public void validateParticipants(Long senderId) {
        Set<Long> participants = Set.of(higherId, lowerId);
        // 내가 해당 채팅방에 참여하지 않은 경우
        boolean contains = participants.contains(senderId);
        if (!contains) {
            throw new ApplicationException(NO_AUTHORITY_FOR_CHAT_ROOM);
        }
    }

    public Long getTargetId(Long senderId) {
        if (higherId.equals(senderId)) {
            return lowerId;
        }
        return higherId;
    }
}
