package cupid.async.event;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDomainEventRepository extends JpaRepository<TestDomainEvent, Long> {
    Optional<TestDomainEvent> findByUuid(String uuid);

    default TestDomainEvent getByUuid(String uuid) {
        return findByUuid(uuid).orElseThrow();
    }
}
