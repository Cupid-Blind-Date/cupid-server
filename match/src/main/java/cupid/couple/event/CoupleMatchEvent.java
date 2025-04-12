package cupid.couple.event;

import static lombok.AccessLevel.PROTECTED;

import cupid.common.event.DomainEvent;
import cupid.common.kafka.topic.KafkaTopic;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@DiscriminatorValue("CoupleMatchEvent")
public class CoupleMatchEvent extends DomainEvent {

    public CoupleMatchEvent(Long targetDomainId) {
        super(targetDomainId);
    }

    @Override
    public String getTopic() {
        return KafkaTopic.COUPLE_MATCH_EVENT_TOPIC;
    }
}
