package cupid.filter.exception;

import cupid.common.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum FilterExceptionCode implements ExceptionCode {

    NOT_FOUND_FILTER(HttpStatus.NOT_FOUND, "F0", "아직 필터를 설정하지 않은 회원입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    FilterExceptionCode(HttpStatus httpStatus, String code, String message) {
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
