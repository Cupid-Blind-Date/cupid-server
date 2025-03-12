package cupid.couple.event;

import static lombok.AccessLevel.PROTECTED;

import cupid.evnet.DomainEvent;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@DiscriminatorValue("CoupleMatchEvent")
public class CoupleMatchEvent extends DomainEvent {

    private static final String TOPIC = "CoupleMatchEvent";

    public CoupleMatchEvent(Long coupleId) {
        super(coupleId);
    }

    @Override
    public String getTopic() {
        return TOPIC;
    }
}
