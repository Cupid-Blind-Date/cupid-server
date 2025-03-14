package cupid.kafka.deadletter;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeadLetterRepository extends JpaRepository<DeadLetter, Long> {

    @Query("SELECT dl FROM DeadLetter dl WHERE dl.uuid = :uuid")
    Optional<DeadLetter> findByUuid(String uuid);
}
