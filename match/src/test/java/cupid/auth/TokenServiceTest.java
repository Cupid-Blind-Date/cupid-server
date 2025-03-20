package cupid.auth;


import static cupid.common.auth.TokenExceptionCode.EXPIRED_TOKEN;
import static cupid.common.auth.TokenExceptionCode.INVALID_TOKEN;

import cupid.common.auth.Token;
import cupid.common.auth.TokenProperty;
import cupid.common.auth.TokenService;
import cupid.support.UnitTest;
import org.junit.jupiter.api.Test;

class TokenServiceTest extends UnitTest {

    private TokenService tokenService = new TokenService(new TokenProperty("sdadsasad".repeat(10), 100000000));

    @Test
    void 만료된_토큰_사용_시_예외() {
        // given
        TokenService tokenService = new TokenService(new TokenProperty("sdadsasad".repeat(10), 1));

        // when
        Token token = tokenService.createToken(1L);

        // then
        exceptionTest(() -> {
            tokenService.extractMemberId(token.accessToken());
        }, EXPIRED_TOKEN);
    }

    @Test
    void 토큰_형식이_올바르지_않으면_예외() {
        // given
        Token token = new Token("dsadsa");

        // when & then
        exceptionTest(() -> {
            tokenService.extractMemberId(token.accessToken());
        }, INVALID_TOKEN);
    }

    @Test
    void 토큰_내용이_올바르지_않은_경우_예외() {
        // given
        Token token = new Token("dsadsa.b.c");

        // when & then
        exceptionTest(() -> {
            tokenService.extractMemberId(token.accessToken());
        }, INVALID_TOKEN);
    }
}
