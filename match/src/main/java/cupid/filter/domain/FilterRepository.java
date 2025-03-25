package cupid.filter.domain;

import static cupid.filter.exception.FilterExceptionCode.NOT_FOUND_FILTER;

import cupid.common.exception.ApplicationException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, Long> {

    Optional<Filter> findByMemberId(Long memberId);

    default Filter getByMemberId(Long memberId) {
        return findByMemberId(memberId).orElseThrow(() -> new ApplicationException(NOT_FOUND_FILTER));
    }

    default boolean existsByMemberId(Long memberId) {
        return findByMemberId(memberId).isPresent();
    }
}
