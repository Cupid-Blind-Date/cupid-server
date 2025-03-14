package cupid.evnet.mock;

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
    public TestEvent eventOccur(Long testId) {
        TestEvent event = new TestEvent(testId);
        applicationEventPublisher.publishEvent(event);
        return event;
    }
}
