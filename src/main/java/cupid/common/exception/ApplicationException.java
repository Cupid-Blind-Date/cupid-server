package cupid.common.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ExceptionCode code;

    public ApplicationException(ExceptionCode code) {
        this.code = code;
    }
}
