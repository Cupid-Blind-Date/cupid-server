package cupid.couple.domain;

import static cupid.common.SQLRestrictionClause.DELETED_AT_IS_NULL;
import static cupid.couple.exception.CoupleExceptionCode.SAME_SENDER_TARGET;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import cupid.common.domain.SoftDeletedDomain;
import cupid.common.exception.ApplicationException;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction(DELETED_AT_IS_NULL)
@SQLDelete(sql = "UPDATE arrow SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(
        name = "arrow",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_arrow_sender_id_target_id", columnNames = {"senderId", "targetId"})
        }
)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Arrow extends SoftDeletedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long targetId;

    @Enumerated(STRING)
    private LikeType likeType;

    public Arrow(Long senderId, Long targetId, LikeType likeType) {
        validateSame(senderId, targetId);
        this.senderId = senderId;
        this.targetId = targetId;
        this.likeType = likeType;
    }

    private void validateSame(Long senderId, Long targetId) {
        if (Objects.equals(senderId, targetId)) {
            throw new ApplicationException(SAME_SENDER_TARGET);
        }
    }

    public Couple match() {
        long higherId = Math.max(senderId, targetId);
        long lowerId = Math.min(senderId, targetId);
        return new Couple(higherId, lowerId);
    }
}
