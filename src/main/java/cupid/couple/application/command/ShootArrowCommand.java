package cupid.couple.application.command;

import cupid.couple.domain.Arrow;
import cupid.couple.domain.LikeType;

public record ShootArrowCommand(
        Long senderId,
        Long targetId
) {
    public Arrow toArrow(LikeType likeType) {
        return new Arrow(senderId, targetId, likeType);
    }
}
