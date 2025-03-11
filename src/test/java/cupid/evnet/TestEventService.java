package cupid.evnet;

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
    public void eventOccur(Long testId) {
        applicationEventPublisher.publishEvent(new TestDomainEvent(testId));
    }
}
