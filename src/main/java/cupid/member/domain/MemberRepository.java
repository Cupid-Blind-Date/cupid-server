package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.NOT_FOUND_MEMBER;

import cupid.common.exception.ApplicationException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getById(Long id) {
        return findById(id).orElseThrow(() ->
                new ApplicationException(NOT_FOUND_MEMBER));
    }

    Optional<Member> findByUsername(String username);
}
