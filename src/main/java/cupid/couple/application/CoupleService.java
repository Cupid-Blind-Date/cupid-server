package cupid.couple.application;

import cupid.couple.application.command.LikeCommand;
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
    public MatchResult like(LikeCommand command) {
        Arrow arrow = command.toArrow(LikeType.LIKE);
        arrowRepository.save(arrow);
        return cupid.match(arrow);
    }

    /**
     * 싫어요 요청
     */
    public void dislike(LikeCommand command) {
        Arrow arrow = command.toArrow(LikeType.DISLIKE);
        arrowRepository.save(arrow);
    }
}
