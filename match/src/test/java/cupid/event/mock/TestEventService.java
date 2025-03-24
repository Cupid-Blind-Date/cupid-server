package cupid.event.mock;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestEventService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public TestEventService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TestDomainEvent eventOccur(Long testId) {
        TestDomainEvent event = new TestDomainEvent(testId);
        applicationEventPublisher.publishEvent(event);
        return event;
    }
}
