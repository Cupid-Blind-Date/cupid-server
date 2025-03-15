package cupid.chat.presentation.websocket.config;

import static cupid.common.auth.Token.BEARER_PREFIX;

import cupid.common.auth.TokenExceptionCode;
import cupid.common.auth.TokenService;
import cupid.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
public class WebsocketAuthInterceptor implements ChannelInterceptor {

    private final TokenService tokenService;

    // https://docs.spring.io/spring-framework/reference/web/websocket/stomp/authentication-token-based.html 참고
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (headerAccessor.getCommand() != StompCommand.CONNECT) {
            // 연결이 아닌 경우
            return message;
        }

        String bearerToken = headerAccessor.getNativeHeader("Authorization").getFirst();
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            String token = bearerToken.substring(7);
            Long memberId = tokenService.extractMemberId(token);
            headerAccessor.addNativeHeader("memberId", String.valueOf(memberId));
            return message;
        }

        throw new ApplicationException(TokenExceptionCode.REQUIRED_TOKEN);
    }
}
