package cupid.test.requiresnewdeadlock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class TestAfterCommitListener {

    private final Logger log = LoggerFactory.getLogger(TestAfterCommitListener.class);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(TestEvent event) {
        log.info("call TestAfterCommitListener.publishEvent(). event-id: {}", event.getId());
    }
}
