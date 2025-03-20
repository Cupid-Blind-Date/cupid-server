package cupid.common.auth;

import cupid.common.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenService tokenService;
    private final BearerTokenExtractor bearerTokenExtractor;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class)
               && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Long resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = extractAccessToken(request);
        return tokenService.extractMemberId(token);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) {
            throw new ApplicationException(TokenExceptionCode.REQUIRED_BEARER_TOKEN);
        }
        return bearerTokenExtractor.extract(bearerToken);
    }
}
