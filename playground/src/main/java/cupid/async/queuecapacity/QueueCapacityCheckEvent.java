package cupid.async.queuecapacity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import cupid.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("QueueCapacityCheckEvent")
public class QueueCapacityCheckEvent extends TestDomainEvent {

    public QueueCapacityCheckEvent(Long requestId) {
        super(requestId);
    }
}
