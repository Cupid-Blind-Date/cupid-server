package test.async.virtualthread;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("VirtualThreadBlockEvent")
public class VirtualThreadBlockEvent extends TestDomainEvent {

    public VirtualThreadBlockEvent(Long requestId) {
        super(requestId);
    }
}
