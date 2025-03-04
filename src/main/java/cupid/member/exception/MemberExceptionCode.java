package cupid.member.exception;

import cupid.common.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum MemberExceptionCode implements ExceptionCode {

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "M0", "아이디 혹은 비밀번호가 잘못되어 로그인에 실패하였습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "M1", "아이디가 중복되었습니다. 다른 아이디를 사용해주세요."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "M2", "해당 id를 가진 회원이 없습니다."),
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
