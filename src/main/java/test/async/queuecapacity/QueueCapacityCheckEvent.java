package test.async.queuecapacity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("QueueCapacityCheckEvent")
public class QueueCapacityCheckEvent extends TestDomainEvent {

    public QueueCapacityCheckEvent(Long requestId) {
        super(requestId);
    }
}
