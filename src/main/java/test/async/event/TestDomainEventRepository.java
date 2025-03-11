package test.async.event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDomainEventRepository extends JpaRepository<TestDomainEvent, Long> {
}
