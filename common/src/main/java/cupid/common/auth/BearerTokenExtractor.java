package cupid.common.auth;

import cupid.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class BearerTokenExtractor {

    public static final String BEARER_PREFIX = "Bearer ";

    public String extract(String bearerToken) {
        log.info("Try to get Bearer Token");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            String token = bearerToken.substring(7);
            log.info("Successfully get Bearer Token");
            return token;
        }
        throw new ApplicationException(TokenExceptionCode.REQUIRED_BEARER_TOKEN);
    }
}
