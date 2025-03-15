package cupid.couple.domain;

import static cupid.couple.exception.CoupleExceptionCode.NOT_FOUND_COUPLE;

import cupid.common.exception.ApplicationException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoupleRepository extends JpaRepository<Couple, Long> {

    default Couple getById(Long id) {
        return findById(id).orElseThrow(() -> {
            throw new ApplicationException(NOT_FOUND_COUPLE);
        });
    }
}
