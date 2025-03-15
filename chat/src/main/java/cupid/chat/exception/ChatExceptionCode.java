package cupid.chat.exception;

import cupid.common.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum ChatExceptionCode implements ExceptionCode {

    NOT_COUNT_CHAT_ROOM(HttpStatus.NOT_FOUND, "CHAT0", "해당 id를 가진 채팅방이 없습니다."),
    NOT_COUNT_CHAT_MESSAGE(HttpStatus.NOT_FOUND, "CHAT1", "해당 id를 가진 채팅이 없습니다."),
    NO_AUTHORITY_TO_SEND_MESSAGE(HttpStatus.FORBIDDEN, "CHAT2", "해당 채팅방에 메세지를 보낼 권한이 없습니다."),
    INVALID_PARTICIPATION_ID(HttpStatus.BAD_REQUEST, "CHAT3", "해당 채팅방에 참여한 사용자에게만 메세지를 보낼 수 있습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ChatExceptionCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
