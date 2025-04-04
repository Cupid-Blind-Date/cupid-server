package cupid.common.auth;

import cupid.common.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum TokenExceptionCode implements ExceptionCode {

    REQUIRED_TOKEN(HttpStatus.UNAUTHORIZED, "T0", "토근 정보가 없습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "T1", "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "T2", "유효하지 않은 토큰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "T3", "인증되지 않았습니다."),
    REQUIRED_BEARER_TOKEN(HttpStatus.UNAUTHORIZED, "T4", "Bearer 토근 정보가 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    TokenExceptionCode(HttpStatus httpStatus, String code, String message) {
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
