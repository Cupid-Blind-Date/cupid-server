package cupid.chat.presentation.websocket.exception;

import cupid.common.exception.ApplicationException;
import cupid.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class WebsocketBusinessExceptionHandler {

    @MessageExceptionHandler
    @SendToUser(value = "/queue/errors", broadcast = false)
    public WebsocketExceptionResponse handleCustomException(ApplicationException exception) {
        ExceptionCode code = exception.getCode();
        log.info("Exception occurred. code: {}, message: {}", code.getCode(), code.getMessage());
        return WebsocketExceptionResponse.from(exception);
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser(value = "/queue/errors", broadcast = false)
    public WebsocketExceptionResponse handleException(Exception e) {
        log.error("Unexpected exception in websocket", e);
        return new WebsocketExceptionResponse("SERVER_ERROR", "4500", null, "SERVER_ERROR");
    }
}
