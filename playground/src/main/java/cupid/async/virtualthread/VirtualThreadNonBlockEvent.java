package cupid.async.virtualthread;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import cupid.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("VirtualThreadNonBlockEvent")
public class VirtualThreadNonBlockEvent extends TestDomainEvent {

    public VirtualThreadNonBlockEvent(Long requestId) {
        super(requestId);
    }
}
