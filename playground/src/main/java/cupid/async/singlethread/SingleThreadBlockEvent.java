package cupid.async.singlethread;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import cupid.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("SingleThreadBlockEvent")
public class SingleThreadBlockEvent extends TestDomainEvent {

    public SingleThreadBlockEvent(Long requestId) {
        super(requestId);
    }
}
