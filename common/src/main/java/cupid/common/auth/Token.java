package cupid.common.auth;

public record Token(
        String accessToken
) {
    public static final String BEARER_PREFIX = "Bearer ";
}
