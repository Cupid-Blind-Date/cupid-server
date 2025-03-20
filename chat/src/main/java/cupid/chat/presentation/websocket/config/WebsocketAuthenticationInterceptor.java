package cupid.chat.presentation.websocket.config;

import static cupid.chat.presentation.websocket.utils.StompHeaderAccessorUtils.putMemberId;

import cupid.chat.presentation.websocket.utils.StompHeaderAccessorUtils;
import cupid.common.auth.BearerTokenExtractor;
import cupid.common.auth.TokenExceptionCode;
import cupid.common.auth.TokenService;
import cupid.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebsocketAuthenticationInterceptor implements ChannelInterceptor {

    private final BearerTokenExtractor bearerTokenExtractor;
    private final TokenService tokenService;

    // https://docs.spring.io/spring-framework/reference/web/websocket/stomp/authentication-token-based.html 참고
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("Try to authenticate Websocket Request");
        StompHeaderAccessor headerAccessor = StompHeaderAccessorUtils.getStompHeaderAccessor(message);

        // 최초 연결 시점인 경우
        StompCommand command = headerAccessor.getCommand();
        if (command == StompCommand.CONNECT) {
            authentication(headerAccessor);
            return message;
        }
        log.info("No need to authenticate Websocket Request");
        return message;
    }

    private void authentication(StompHeaderAccessor headerAccessor) {
        log.info("Try to authentication");
        String bearerToken = headerAccessor.getFirstNativeHeader("Authorization");
        if (bearerToken == null) {
            throw new ApplicationException(TokenExceptionCode.REQUIRED_BEARER_TOKEN);
        }
        String token = bearerTokenExtractor.extract(bearerToken);
        Long memberId = tokenService.extractMemberId(token);
        // 세션에 memberId 저장
        putMemberId(headerAccessor, memberId);
        log.info("WebSocket Connected. memberId: {}", memberId);
    }
}
