package cupid.couple.exception;

import cupid.common.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum CoupleExceptionCode implements ExceptionCode {

    NOT_FOUND_COUPLE(HttpStatus.NOT_FOUND, "C0", "해당 id를 가진 커플이 없습니다."),
    DUPLICATED_COUPLE(HttpStatus.CONFLICT, "C1", "이미 매치된 커플입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    CoupleExceptionCode(HttpStatus httpStatus, String code, String message) {
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
