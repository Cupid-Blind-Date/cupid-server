package cupid.common.event;

import static cupid.common.event.exception.DomainEventExceptionCode.NOT_FOUND_DOMAIN_EVENT;

import cupid.common.exception.ApplicationException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainEventRepository extends JpaRepository<DomainEvent, Long> {

    default DomainEvent getById(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException(NOT_FOUND_DOMAIN_EVENT));
    }

    Optional<DomainEvent> findByUuid(String uuid);

    default DomainEvent getByUuid(String uuid) {
        return findByUuid(uuid).orElseThrow(() -> new ApplicationException(NOT_FOUND_DOMAIN_EVENT));
    }
}
