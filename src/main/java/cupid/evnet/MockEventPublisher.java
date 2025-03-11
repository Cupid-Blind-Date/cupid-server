package cupid.evnet;

import org.springframework.stereotype.Component;

@Component
public class MockEventPublisher implements EventPublisher {
    @Override
    public void publish(DomainEvent domainEvent) {

    }
}
