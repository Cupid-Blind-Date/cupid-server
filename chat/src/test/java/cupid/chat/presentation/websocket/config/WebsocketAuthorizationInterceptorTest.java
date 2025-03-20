package cupid.chat.presentation.websocket.config;

import static cupid.chat.exception.ChatExceptionCode.NO_AUTHORITY_FOR_CHAT_ROOM;
import static cupid.chat.exception.ChatExceptionCode.UNAUTHENTICATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.chat.domain.ChatRoom;
import cupid.chat.domain.ChatRoomRepository;
import cupid.common.exception.ApplicationException;
import cupid.common.exception.ExceptionCode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

@DisplayName("웹소켓 인가 인터셉터 (WebsocketAuthorizationInterceptor) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class WebsocketAuthorizationInterceptorTest {

    private ChatRoomRepository chatRoomRepository = mock(ChatRoomRepository.class);

    private WebsocketAuthorizationInterceptor websocketAuthenticationInterceptor = new WebsocketAuthorizationInterceptor(
            chatRoomRepository
    );
    private Message<?> message = mock(Message.class);
    private MessageChannel channel = mock(MessageChannel.class);

    @Test
    void CONNECT_시점에_인가를_수행한다() {
        // given
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(
                StompCommand.CONNECT,
                Map.of("RoomId", List.of("2"))
        );
        stompHeaderAccessor.setSessionAttributes(new HashMap<>());
        stompHeaderAccessor.getSessionAttributes().put("memberId", 1L);
        MessageHeaders messageHeaders = stompHeaderAccessor.getMessageHeaders();
        given(message.getHeaders()).willReturn(messageHeaders);
        given(chatRoomRepository.getById(2L)).willReturn(new ChatRoom(3L, 1L));

        // when
        websocketAuthenticationInterceptor.preSend(message, channel);

        // then
        assertThat(stompHeaderAccessor.getSessionAttributes().get("memberId")).isEqualTo(1L);
        assertThat(stompHeaderAccessor.getSessionAttributes().get("targetId")).isEqualTo(3L);
        assertThat(stompHeaderAccessor.getSessionAttributes().get("roomId")).isEqualTo(2L);
    }

    @Test
    void 인가_수행시_memberId가_세션에_없으면_예외() {
        // given
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(
                StompCommand.CONNECT,
                Map.of("RoomId", List.of("1"))
        );
        stompHeaderAccessor.setSessionAttributes(new HashMap<>());
        given(message.getHeaders()).willReturn(stompHeaderAccessor.getMessageHeaders());
        given(chatRoomRepository.getById(1L)).willReturn(new ChatRoom(2L, 1L));

        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class, () -> {
            websocketAuthenticationInterceptor.preSend(message, channel);
        }).getCode();
        assertThat(code).isEqualTo(UNAUTHENTICATED);
    }

    @Test
    void 인가_수행시_채팅방_접근_권한이_없으면_예외() {
        // given
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(
                StompCommand.CONNECT,
                Map.of("RoomId", List.of("1"))
        );
        stompHeaderAccessor.setSessionAttributes(new HashMap<>());
        stompHeaderAccessor.getSessionAttributes().put("memberId", 3L);
        given(message.getHeaders()).willReturn(stompHeaderAccessor.getMessageHeaders());
        given(chatRoomRepository.getById(1L)).willReturn(new ChatRoom(2L, 1L));

        // when
        ExceptionCode code = assertThrows(ApplicationException.class, () -> {
            websocketAuthenticationInterceptor.preSend(message, channel);
        }).getCode();
        assertThat(code).isEqualTo(NO_AUTHORITY_FOR_CHAT_ROOM);
    }

    @Test
    void CONNECT_시점이_아니면_스킵() {
        // given
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(
                StompCommand.SUBSCRIBE,
                Map.of("Authorization", List.of("Bearer jwt"))
        );
        stompHeaderAccessor.setSessionAttributes(new HashMap<>());
        MessageHeaders messageHeaders = stompHeaderAccessor.getMessageHeaders();
        given(message.getHeaders()).willReturn(messageHeaders);

        // when
        websocketAuthenticationInterceptor.preSend(message, channel);

        // then
        verify(chatRoomRepository, times(0))
                .getById(anyLong());
    }
}
