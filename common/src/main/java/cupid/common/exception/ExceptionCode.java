package cupid.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {

    String name();

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
