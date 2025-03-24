package cupid.member.presentation.response;

public record LoginResponse(
        Long memberId,
        String accessToken
) {
}
