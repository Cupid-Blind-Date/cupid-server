package cupid.chat.domain;

import static cupid.common.SQLRestrictionClause.DELETED_AT_IS_NULL;

import cupid.common.domain.SoftDeletedDomain;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction(DELETED_AT_IS_NULL)
@SQLDelete(sql = "UPDATE chat_message SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatMessage extends SoftDeletedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatRoomId;

    private Long senderId;
    private Long targetId;

    private String message;

    @Enumerated(EnumType.STRING)
    private ChatMessageType chatMessageType;

    private boolean read;

    public ChatMessage(Long chatRoomId, Long senderId, Long targetId, String message, ChatMessageType chatMessageType) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.targetId = targetId;
        this.message = message;
        this.chatMessageType = chatMessageType;
        this.read = false;
    }

    public void read() {
        this.read = true;
    }
}
