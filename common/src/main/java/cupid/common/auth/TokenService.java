package cupid.common.auth;

import static cupid.common.auth.TokenExceptionCode.EXPIRED_TOKEN;
import static cupid.common.auth.TokenExceptionCode.INVALID_TOKEN;

import cupid.common.exception.ApplicationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class TokenService {

    private static final String MEMBER_ID_CLAIM = "memberId";

    private final SecretKey secretKey;
    private final long accessTokenExpirationMillis;

    public TokenService(
            TokenProperty tokenProperty
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(tokenProperty.secretKey()));
        this.accessTokenExpirationMillis = tokenProperty.accessTokenExpirationMillis();
    }

    public Token createToken(Long memberId) {
        String accessToken = createAccessToken(memberId);
        return new Token(accessToken);
    }

    private String createAccessToken(Long memberId) {
        return Jwts.builder()
                .claim(MEMBER_ID_CLAIM, memberId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMillis))
                .signWith(secretKey, SIG.HS512)
                .compact();
    }

    public Long extractMemberId(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(MEMBER_ID_CLAIM, Long.class);
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(EXPIRED_TOKEN);
        } catch (MalformedJwtException e) {
            throw new ApplicationException(INVALID_TOKEN);
        } catch (Exception e) {
            throw new ApplicationException(INVALID_TOKEN);
        }
    }
}
