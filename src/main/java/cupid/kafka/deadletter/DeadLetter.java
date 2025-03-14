package cupid.kafka.deadletter;

import cupid.common.domain.SoftDeletedDomain;
import cupid.kafka.KafkaDomainEventMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DeadLetter extends SoftDeletedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    private String failReason;

    private boolean recovered;

    public DeadLetter(String uuid, String failReason, boolean recovered) {
        this.uuid = uuid;
        this.failReason = failReason;
        this.recovered = recovered;
    }

    public static DeadLetter create(
            KafkaDomainEventMessage message,
            String failReason
    ) {
        return new DeadLetter(message.uuid(), failReason, false);
    }

    public void recover() {
        this.recovered = true;
    }
}
