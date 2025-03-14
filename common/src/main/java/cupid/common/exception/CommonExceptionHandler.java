package cupid.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ExceptionResponse> handleApplicationException(ApplicationException e) {
        ExceptionCode code = e.getCode();
        log.info("Exception occurred. code: {}, message: {}", code.getCode(), code.getMessage());
        return ResponseEntity.status(code.getHttpStatus())
                .body(ExceptionResponse.from(code));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(defaultMessage);
    }
}
