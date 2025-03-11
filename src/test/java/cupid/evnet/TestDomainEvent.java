package cupid.evnet;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@DiscriminatorValue("TestDomainEvent")
@Entity
public class TestDomainEvent extends DomainEvent {

    private Long testId;

    public TestDomainEvent() {
    }

    public TestDomainEvent(Long testId) {
        this.testId = testId;
    }

    public TestDomainEvent(Long testId, String uuid, EventState state) {
        super(uuid, state);
        this.testId = testId;
    }
}
