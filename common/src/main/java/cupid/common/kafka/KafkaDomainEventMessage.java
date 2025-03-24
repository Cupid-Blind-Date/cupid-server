package cupid.common.kafka;

import cupid.common.event.DomainEvent;

public record KafkaDomainEventMessage(
        Long id,
        String uuid,
        Long targetDomainId
) {
    public static KafkaDomainEventMessage from(DomainEvent domainEvent) {
        return new KafkaDomainEventMessage(
                domainEvent.getId(),
                domainEvent.getUuid(),
                domainEvent.getTargetDomainId()
        );
    }
}
