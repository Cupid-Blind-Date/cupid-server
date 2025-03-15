package cupid.chat.presentation.websocket.exception;

import cupid.common.exception.ApplicationException;
import cupid.common.exception.ExceptionCode;

public record WebsocketExceptionResponse(
        String name,
        String code,
        String applicationCode,
        String message
) {
    public static WebsocketExceptionResponse from(ApplicationException e) {
        ExceptionCode code = e.getCode();
        return new WebsocketExceptionResponse(
                code.name(),
                WebsocketExceptionCodeUtils.createCode(e.getCode().getHttpStatus()),
                code.getCode(),
                code.getMessage()
        );
    }
}
