package test.virtualthread;

import cupid.evnet.DomainEvent;
import cupid.evnet.EventState;
import jakarta.persistence.DiscriminatorValue;

@DiscriminatorValue("VirtualThreadEvent")
public class VirtualThreadEvent extends DomainEvent {
    public VirtualThreadEvent() {
    }

    public VirtualThreadEvent(String uuid, EventState state) {
        super(uuid, state);
    }
}
