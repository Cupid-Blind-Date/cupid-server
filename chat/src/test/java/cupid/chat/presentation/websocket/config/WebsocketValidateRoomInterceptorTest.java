package cupid.chat.presentation.websocket.config;

import static cupid.chat.exception.ChatExceptionCode.NO_AUTHORITY_FOR_CHAT_ROOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.DESTINATION_HEADER;

import cupid.common.exception.ApplicationException;
import cupid.common.exception.ExceptionCode;
import cupid.support.UnitTest;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

@DisplayName("웹소켓 방 검증 인터셉터 (WebsocketValidateRoomInterceptor) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class WebsocketValidateRoomInterceptorTest extends UnitTest {

    private WebsocketValidateRoomInterceptor websocketValidateRoomInterceptor = new WebsocketValidateRoomInterceptor();

    private Message<?> message = mock(Message.class);
    private MessageChannel channel = mock(MessageChannel.class);
    private Long memberId = 1L;
    private Long roomId = 2L;

    @EnumSource(value = StompCommand.class, mode = Mode.INCLUDE, names = {"SEND", "SUBSCRIBE"})
    @ParameterizedTest
    void SEND_혹은_SUBSCRIBE_시점에_채팅방_검증을_진행한다(StompCommand command) {
        // given
        Map<StompCommand, String> commandDestinationMap = Map.of(
                StompCommand.SEND, "/pub/chat/" + roomId,
                StompCommand.SUBSCRIBE, "/sub/chat/" + roomId
        );
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(command);
        stompHeaderAccessor.setHeader(DESTINATION_HEADER, commandDestinationMap.get(command));
        stompHeaderAccessor.setSessionAttributes(Map.of(
                "memberId", memberId,
                "roomId", roomId
        ));
        given(message.getHeaders()).willReturn(stompHeaderAccessor.getMessageHeaders());

        // when & then
        Assertions.assertDoesNotThrow(() -> {
            websocketValidateRoomInterceptor.preSend(message, channel);
        });
    }

    @EnumSource(value = StompCommand.class, mode = Mode.INCLUDE, names = {"SEND", "SUBSCRIBE"})
    @ParameterizedTest
    void CONNECT_시점에서의_roomId와_현재_요청에서의_roomId가_다르면_예외(StompCommand command) {
        // given
        String destinationRoomId = "3";
        Map<StompCommand, String> commandDestinationMap = Map.of(
                StompCommand.SEND, "/pub/chat/" + destinationRoomId,
                StompCommand.SUBSCRIBE, "/sub/chat/" + destinationRoomId
        );

        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(command);
        stompHeaderAccessor.setHeader(DESTINATION_HEADER, commandDestinationMap.get(command));
        stompHeaderAccessor.setSessionAttributes(Map.of(
                "memberId", memberId,
                "roomId", roomId
        ));
        given(message.getHeaders()).willReturn(stompHeaderAccessor.getMessageHeaders());

        // when & then
        ExceptionCode code = Assertions.assertThrows(ApplicationException.class, () -> {
            websocketValidateRoomInterceptor.preSend(message, channel);
        }).getCode();
        assertThat(code).isEqualTo(NO_AUTHORITY_FOR_CHAT_ROOM);
    }

    @EnumSource(value = StompCommand.class, mode = Mode.EXCLUDE, names = {"SEND", "SUBSCRIBE"})
    @ParameterizedTest
    void command가_publish_혹은_subscribe_가_아니라면_스킵(StompCommand command) {
        // given
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(command);
        given(message.getHeaders()).willReturn(stompHeaderAccessor.getMessageHeaders());

        // when & then
        Assertions.assertDoesNotThrow(() -> {
            websocketValidateRoomInterceptor.preSend(message, channel);
        });
    }

    @EnumSource(value = StompCommand.class, mode = Mode.INCLUDE, names = {"SEND", "SUBSCRIBE"})
    @ParameterizedTest
    void 경로가_채팅방_구독_주소가_아니라면_스킵(StompCommand command) {
        // given
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(command);
        stompHeaderAccessor.setHeader(DESTINATION_HEADER, "/pub/other");
        given(message.getHeaders()).willReturn(stompHeaderAccessor.getMessageHeaders());

        // when & then
        Assertions.assertDoesNotThrow(() -> {
            websocketValidateRoomInterceptor.preSend(message, channel);
        });
    }
}
