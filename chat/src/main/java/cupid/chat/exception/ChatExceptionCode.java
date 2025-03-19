package cupid.chat.exception;

import cupid.common.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum ChatExceptionCode implements ExceptionCode {

    NOT_COUNT_CHAT_ROOM(HttpStatus.NOT_FOUND, "CHAT0", "해당 id를 가진 채팅방이 없습니다."),
    NOT_COUNT_CHAT_MESSAGE(HttpStatus.NOT_FOUND, "CHAT1", "해당 id를 가진 채팅이 없습니다."),
    NO_AUTHORITY_FOR_CHAT_ROOM(HttpStatus.FORBIDDEN, "CHAT2", "해당 채팅방에 대한 권한이 없습니다."),
    INVALID_PARTICIPATION_ID(HttpStatus.BAD_REQUEST, "CHAT3", "해당 채팅방에 참여한 사용자에게만 메세지를 보낼 수 있습니다."),
    INVALID_CHAT_SUBSCRIBE_URL(HttpStatus.BAD_REQUEST, "CHAT4", "잘못된 채팅방 메세지 구독 주소입니다."),
    INVALID_CHAT_ROOM_ID(HttpStatus.BAD_REQUEST, "CHAT5", "잘못된 채팅방 id입니다."),
    NOT_FOUNT_CHAT_ROOM_ID(HttpStatus.BAD_REQUEST, "CHAT6", "채팅방 id 가 없습니다."),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "CHAT7", "인증되지 않은 사용자입니다."),
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
