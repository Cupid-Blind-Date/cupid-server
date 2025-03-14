package cupid.common.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ExceptionCode code;

    public ApplicationException(ExceptionCode code) {
        super("exception: %s, code: %s, message: %s".formatted(
                code,
                code.getCode(),
                code.getMessage()));
        this.code = code;
    }
}
