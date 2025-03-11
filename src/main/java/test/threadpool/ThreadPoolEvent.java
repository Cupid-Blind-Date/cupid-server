package test.threadpool;

import cupid.evnet.DomainEvent;
import cupid.evnet.EventState;
import jakarta.persistence.DiscriminatorValue;

@DiscriminatorValue("VirtualThreadEvent")
public class ThreadPoolEvent extends DomainEvent {
    public ThreadPoolEvent() {
    }

    public ThreadPoolEvent(String uuid, EventState state) {
        super(uuid, state);
    }
}
