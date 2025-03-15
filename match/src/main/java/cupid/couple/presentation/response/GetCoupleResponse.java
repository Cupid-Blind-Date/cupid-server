package cupid.couple.presentation.response;

import cupid.couple.domain.Couple;

public record GetCoupleResponse(
        Long coupleId,
        Long higherId,
        Long lowerId
) {
    public static GetCoupleResponse from(Couple couple) {
        return new GetCoupleResponse(
                couple.getId(),
                couple.getHigherId(),
                couple.getLowerId()
        );
    }
}
