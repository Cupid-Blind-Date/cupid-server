package cupid.common.evnet;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@DiscriminatorValue("CoupleMatchEvent")
public class CoupleMatchEvent extends DomainEvent {

    public static final String COUPLE_MATCH_EVENT_TOPIC = "CoupleMatchEvent";

    public CoupleMatchEvent(Long targetDomainId) {
        super(targetDomainId);
    }

    @Override
    public String getTopic() {
        return COUPLE_MATCH_EVENT_TOPIC;
    }
}
