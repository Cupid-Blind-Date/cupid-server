package cupid.evnet.mock;

import cupid.evnet.DomainEvent;
import cupid.evnet.EventState;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TestEvent")
public class TestEvent extends DomainEvent {

    private String topic;

    public TestEvent() {
    }

    public TestEvent(Long testId, String topic) {
        super(testId);
        this.topic = topic;
    }

    public TestEvent(Long testId) {
        super(testId);
    }

    public TestEvent(String uuid, EventState state, Long targetDomainId, String topic) {
        super(uuid, state, targetDomainId);
        this.topic = topic;
    }

    @Override
    public String getTopic() {
        return topic == null ? "TestEvent" : topic;
    }
}
