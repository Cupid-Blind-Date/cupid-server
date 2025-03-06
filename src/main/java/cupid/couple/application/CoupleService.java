package cupid.couple.application;

import cupid.couple.application.command.ShootArrowCommand;
import cupid.couple.domain.Arrow;
import cupid.couple.domain.ArrowRepository;
import cupid.couple.domain.LikeType;
import cupid.couple.domain.service.Cupid;
import cupid.couple.domain.service.MatchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CoupleService {

    private final Cupid cupid;
    private final ArrowRepository arrowRepository;

    /**
     * 좋아요 요청
     */
    public MatchResult shootLikeArrow(ShootArrowCommand command) {
        Arrow arrow = command.toArrow(LikeType.LIKE);
        arrowRepository.save(arrow);
        return cupid.match(arrow);
    }

    /**
     * 싫어요 요청
     */
    public void shootUnLikeArrow(ShootArrowCommand command) {
        Arrow arrow = command.toArrow(LikeType.UNLIKE);
        arrowRepository.save(arrow);
    }
}
