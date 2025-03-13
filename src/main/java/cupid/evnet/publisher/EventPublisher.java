package cupid.evnet.publisher;

import cupid.evnet.DomainEvent;

public interface EventPublisher {

    void publish(DomainEvent domainEvent);
}
