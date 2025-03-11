package test.async.virtualthread;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("VirtualThreadEvent")
public class VirtualThreadEvent extends TestDomainEvent {

    public VirtualThreadEvent(Long requestId) {
        super(requestId);
    }
}
