package cupid.chat.presentation.websocket.exception;

import jakarta.annotation.Nullable;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebsocketExceptionHandler extends StompSubProtocolErrorHandler {

    private final Set<StompExceptionHandler> stompExceptionHandlers;

    @Override
    @Nullable
    public Message<byte[]> handleClientMessageProcessingError(@Nullable Message<byte[]> clientMessage, Throwable ex) {
        log.info("Try to handle websocket exception. e: {}", ex.getClass());
        for (StompExceptionHandler handler : stompExceptionHandlers) {
            if (handler.canHandle(ex)) {
                return handler.handle(clientMessage, ex);
            }
        }
        log.error("Unexpected exception while processing client message", ex);
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }
}
