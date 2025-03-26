package cupid.common.event;

import static cupid.common.SQLRestrictionClause.DELETED_DATE_IS_NULL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;
import static lombok.AccessLevel.PROTECTED;

import cupid.common.domain.SoftDeletedDomain;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction(DELETED_DATE_IS_NULL)
@SQLDelete(sql = "UPDATE domain_event SET deleted_date = CURRENT_TIMESTAMP WHERE id = ?")
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "event_type")
public abstract class DomainEvent extends SoftDeletedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Enumerated(STRING)
    private EventState state;

    @Column(nullable = false)
    private Long targetDomainId;

    private String failReason;

    protected DomainEvent(Long targetDomainId) {
        this.uuid = UUID.randomUUID().toString();
        this.state = EventState.INIT;
        this.targetDomainId = targetDomainId;
    }

    protected DomainEvent(String uuid, EventState state, Long targetDomainId) {
        this.uuid = uuid;
        this.state = state;
        this.targetDomainId = targetDomainId;
    }

    public void regenerateUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public void produceSuccess() {
        this.state = EventState.PRODUCE_SUCCESS;
    }

    public void produceFail(Throwable e) {
        this.state = EventState.PRODUCE_FAIL;
        this.failReason = e.getMessage();
    }

    public abstract String getTopic();
}
