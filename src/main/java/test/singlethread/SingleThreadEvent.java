package test.singlethread;

import cupid.evnet.DomainEvent;
import cupid.evnet.EventState;
import jakarta.persistence.DiscriminatorValue;

@DiscriminatorValue("SingleThreadEvent")
public class SingleThreadEvent extends DomainEvent {
    public SingleThreadEvent() {
    }

    public SingleThreadEvent(String uuid, EventState state) {
        super(uuid, state);
    }
}
