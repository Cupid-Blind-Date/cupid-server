package cupid.common.evnet.publisher;

import cupid.common.evnet.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void publishWithTx(DomainEvent event) {
        log.info("Try to publish domain event with transaction.");
        applicationEventPublisher.publishEvent(event);
    }

    public void publishWithoutTx(DomainEvent event) {
        log.info("Try to publish domain event without transaction.");
        applicationEventPublisher.publishEvent(event);
    }
}
