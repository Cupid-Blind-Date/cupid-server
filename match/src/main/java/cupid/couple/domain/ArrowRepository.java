package cupid.couple.domain;

import static cupid.couple.domain.LikeType.LIKE;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArrowRepository extends JpaRepository<Arrow, Long> {

    @Query("SELECT a FROM Arrow a WHERE a.senderId = :senderId AND a.targetId = :targetId AND a.likeType = :likeType")
    Optional<Arrow> findBySenderIdAndTargetIdAndLikeType(Long senderId, Long targetId, LikeType likeType);

    default boolean existsTargetArrow(Long senderId, Long targetId, LikeType likeType) {
        return findBySenderIdAndTargetIdAndLikeType(senderId, targetId, likeType).isPresent();
    }

    default boolean doesNotExistsTargetLikeArrow(Long senderId, Long targetId) {
        return findBySenderIdAndTargetIdAndLikeType(senderId, targetId, LIKE).isEmpty();
    }
}
