package cupid.support;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class RequestDataTest<T> extends CommonTest {

    protected static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public void validateSuccess(T request) {
        Set<ConstraintViolation<T>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    public void validateFail(T request) {
        Set<ConstraintViolation<T>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    public void validateFailWithMessage(T request) {
        Set<ConstraintViolation<T>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
        violations.forEach(System.out::println);
    }
}
