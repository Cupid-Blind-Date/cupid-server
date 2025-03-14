package cupid.evnet.mock;

import cupid.common.evnet.DomainEvent;
import cupid.common.evnet.EventState;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TestDomainEvent")
public class TestDomainEvent extends DomainEvent {

    private String topic;

    public TestDomainEvent() {
    }

    public TestDomainEvent(Long testId, String topic) {
        super(testId);
        this.topic = topic;
    }

    public TestDomainEvent(Long testId) {
        super(testId);
    }

    public TestDomainEvent(String uuid, EventState state, Long targetDomainId, String topic) {
        super(uuid, state, targetDomainId);
        this.topic = topic;
    }

    @Override
    public String getTopic() {
        return topic == null ? "TestDomainEvent" : topic;
    }
}
