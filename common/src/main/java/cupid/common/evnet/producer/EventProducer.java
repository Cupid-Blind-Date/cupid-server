package cupid.common.evnet.producer;

import cupid.common.evnet.DomainEvent;

public interface EventProducer {

    void produce(DomainEvent domainEvent);
}
