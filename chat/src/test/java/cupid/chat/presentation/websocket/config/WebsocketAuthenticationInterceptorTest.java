package cupid.chat.presentation.websocket.config;

import static cupid.common.auth.TokenExceptionCode.INVALID_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.common.auth.BearerTokenExtractor;
import cupid.common.auth.TokenService;
import cupid.common.exception.ApplicationException;
import cupid.common.exception.ExceptionCode;
import cupid.support.UnitTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

@DisplayName("웹소켓 인증 인터셉터(WebsocketAuthenticationInterceptor) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class WebsocketAuthenticationInterceptorTest extends UnitTest {

    private BearerTokenExtractor bearerTokenExtractor = new BearerTokenExtractor();

    private TokenService tokenService = mock(TokenService.class);

    private WebsocketAuthenticationInterceptor websocketAuthenticationInterceptor = new WebsocketAuthenticationInterceptor(
            bearerTokenExtractor,
            tokenService
    );
    private Message<?> message = mock(Message.class);
    private MessageChannel channel = mock(MessageChannel.class);

    @Test
    void CONNECT_시점에_인증을_수행한다() {
        // given
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(
                StompCommand.CONNECT,
                Map.of("Authorization", List.of("Bearer jwt"))
        );
        stompHeaderAccessor.setSessionAttributes(new HashMap<>());
        given(message.getHeaders()).willReturn(stompHeaderAccessor.getMessageHeaders());
        given(tokenService.extractMemberId("jwt")).willReturn(1L);

        // when
        websocketAuthenticationInterceptor.preSend(message, channel);

        // then
        assertThat(stompHeaderAccessor.getSessionAttributes().get("memberId")).isEqualTo(1L);
    }

    @Test
    void 인증_수행시_JWT가_유효하지_않으면_예외() {
        // given
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(
                StompCommand.CONNECT,
                Map.of("Authorization", List.of("Bearer jwt"))
        );
        stompHeaderAccessor.setSessionAttributes(new HashMap<>());
        given(message.getHeaders()).willReturn(stompHeaderAccessor.getMessageHeaders());
        willThrow(new ApplicationException(INVALID_TOKEN))
                .given(tokenService)
                .extractMemberId("jwt");

        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class, () -> {
            websocketAuthenticationInterceptor.preSend(message, channel);
        }).getCode();
        assertThat(code).isEqualTo(INVALID_TOKEN);
    }

    @Test
    void CONNECT_시점이_아니면_스킵() {
        // given
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(
                StompCommand.SUBSCRIBE,
                Map.of("Authorization", List.of("Bearer jwt"))
        );
        stompHeaderAccessor.setSessionAttributes(new HashMap<>());
        given(message.getHeaders()).willReturn(stompHeaderAccessor.getMessageHeaders());

        // when
        websocketAuthenticationInterceptor.preSend(message, channel);

        // then
        verify(tokenService, times(0))
                .extractMemberId(anyString());
    }
}
