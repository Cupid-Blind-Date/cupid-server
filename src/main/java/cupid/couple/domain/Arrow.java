package cupid.couple.domain;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"senderId", "targetId"})
        }
)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Arrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;
    private Long targetId;

    @Enumerated(STRING)
    private LikeType likeType;

    public Arrow(Long senderId, Long targetId, LikeType likeType) {
        this.senderId = senderId;
        this.targetId = targetId;
        this.likeType = likeType;
    }

    public Couple match() {
        long higherId = Math.max(senderId, targetId);
        long lowerId = Math.min(senderId, targetId);
        return new Couple(higherId, lowerId);
    }
}
