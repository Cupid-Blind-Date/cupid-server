package cupid.async.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestEventRecorder {

    private final TestDomainEventRepository testDomainEventRepository;

    /**
     * uuid 가 중복되는 극악의 경우를 대비하여, 중복되는 경우 uuid 값을 바꾸어 저장한다.
     */
    public TestDomainEvent record(TestDomainEvent testDomainEvent) {
        while (true) {
            try {
                log.info("Try to record event. uuid: {}", testDomainEvent.getUuid());
                TestDomainEvent save = testDomainEventRepository.save(testDomainEvent);
                log.info("Successfully record event. id: {}", save.getId());
                return save;
            } catch (DataIntegrityViolationException e) {
                if (e.getMessage().contains("Unique index or primary key violation")) {
                    log.info("Event uuid duplicated. uuid: {}. so record retry", testDomainEvent);
                    testDomainEvent.regenerateUuid();
                } else {
                    log.error("Unexpected exception occurred when record event. e: {}, message: {}",
                            e.getClass(), e.getMessage());
                    throw e;
                }
            }
        }
    }
}
