package test.async.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TestEventTxService {

    private final ApplicationEventPublisher publisher;

    @Transactional
    public void produce(TestDomainEvent event) {
        publisher.publishEvent(event);
    }
}
