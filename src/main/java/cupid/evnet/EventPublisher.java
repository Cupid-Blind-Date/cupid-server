package cupid.evnet;

public interface EventPublisher {

    void publish(DomainEvent domainEvent);
}
