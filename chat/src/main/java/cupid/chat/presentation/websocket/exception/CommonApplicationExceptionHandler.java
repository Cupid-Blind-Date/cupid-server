package cupid.chat.presentation.websocket.exception;

import static org.springframework.messaging.simp.stomp.DefaultStompSession.EMPTY_PAYLOAD;

import com.fasterxml.jackson.databind.ObjectMapper;
import cupid.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CommonApplicationExceptionHandler implements StompExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public boolean canHandle(Throwable ex) {
        return ex instanceof ApplicationException ae;
    }

    @Override
    public Message<byte[]> handle(Message<byte[]> clientMessage, Throwable cause) {
        StompHeaderAccessor errorHeaderAccessor = StompHeaderAccessor.create(StompCommand.ERROR);

        WebsocketExceptionResponse response = null;
        if (cause instanceof ApplicationException ae) {
            response = WebsocketExceptionResponse.from(ae);
            log.info("Exception occur when connect websocket. response: {}", response);
            errorHeaderAccessor.setMessage(ae.getCode().getCode());
            errorHeaderAccessor.setLeaveMutable(true);
            response = WebsocketExceptionResponse.from(ae);
        }
        return createMessage(errorHeaderAccessor, response);
    }

    private Message<byte[]> createMessage(
            StompHeaderAccessor errorHeaderAccessor,
            WebsocketExceptionResponse response
    ) {
        errorHeaderAccessor.setImmutable();
        if (response == null) {
            return MessageBuilder.createMessage(EMPTY_PAYLOAD, errorHeaderAccessor.getMessageHeaders());
        }
        try {
            byte[] payload = objectMapper.writeValueAsBytes(response);
            return MessageBuilder.createMessage(payload, errorHeaderAccessor.getMessageHeaders());
        } catch (Exception e) {
            log.error("Unexpected exception while create unauthorized error message", e);
            return MessageBuilder.createMessage(EMPTY_PAYLOAD, errorHeaderAccessor.getMessageHeaders());
        }
    }
}
