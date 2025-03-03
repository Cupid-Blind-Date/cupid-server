package cupid.member.exception;

import cupid.common.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum MemberExceptionCode implements ExceptionCode {

    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "M0", "유효하지 않은 닉네임입니다."),
    INVALID_USERNAME(HttpStatus.BAD_REQUEST, "M1", "유효하지 않은 아이디입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "M2", "유효하지 않은 비밀번호입니다."),
    INVALID_AGE(HttpStatus.BAD_REQUEST, "M3", "유효하지 않은 나이입니다."),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "M4", "아이디 혹은 비밀번호가 잘못되어 로그인에 실패하였습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    MemberExceptionCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    public String getMessage() {
        return "";
    }
}
