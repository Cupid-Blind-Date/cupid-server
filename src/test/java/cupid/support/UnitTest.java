package cupid.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cupid.common.exception.ApplicationException;
import cupid.common.exception.ExceptionCode;
import org.junit.jupiter.api.function.Executable;

public class UnitTest extends MockTestSupport {

    public void exceptionTest(Executable executable, ExceptionCode e) {
        ExceptionCode code = assertThrows(ApplicationException.class,
                executable
        ).getCode();
        assertThat(code).isEqualTo(e);
    }
}
