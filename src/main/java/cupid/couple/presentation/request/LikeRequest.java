package cupid.couple.presentation.request;

import cupid.couple.application.command.LikeCommand;

public record LikeRequest(
        Long targetId
) {
    public LikeCommand toCommand(Long memberId) {
        return new LikeCommand(memberId, targetId);
    }
}
