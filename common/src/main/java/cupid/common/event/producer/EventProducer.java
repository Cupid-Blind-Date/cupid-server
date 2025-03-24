package cupid.common.event.producer;

import cupid.common.event.DomainEvent;

public interface EventProducer {

    void produce(DomainEvent domainEvent);
}
