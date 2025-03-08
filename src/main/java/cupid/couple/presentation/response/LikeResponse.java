package cupid.couple.presentation.response;

import cupid.couple.domain.service.MatchResult;

public record LikeResponse(
        MatchResult result
) {
}
