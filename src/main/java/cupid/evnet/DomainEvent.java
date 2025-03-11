package cupid.evnet;

import static jakarta.persistence.InheritanceType.SINGLE_TABLE;
import static lombok.AccessLevel.PROTECTED;

import cupid.common.domain.SoftDeletedDomain;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@DiscriminatorColumn(name = "event_type", length = 255)
@Inheritance(strategy = SINGLE_TABLE)
public abstract class DomainEvent extends SoftDeletedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true)
    protected String uuid;

    @Enumerated(EnumType.STRING)
    protected EventState state;

    protected DomainEvent(String uuid, EventState state) {
        this.uuid = uuid;
        this.state = state;
    }

    public void init() {
        this.uuid = UUID.randomUUID().toString();
        this.state = EventState.INIT;
    }

    public void regenerateUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public void publishSuccess() {
        this.state = EventState.PUBLISH_SUCCESS;
    }

    public void publishFail() {
        this.state = EventState.PUBLISH_FAIL;
    }
}
