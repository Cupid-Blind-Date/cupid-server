package cupid.evnet.producer;

import cupid.evnet.DomainEvent;

public interface EventProducer {

    void produce(DomainEvent domainEvent);
}
