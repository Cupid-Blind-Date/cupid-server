package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.INVALID_USERNAME;

import cupid.common.exception.ApplicationException;
import cupid.common.utils.StringUtils;
import jakarta.persistence.Column;
import java.util.regex.Pattern;

public record Username(
        @Column(nullable = false, length = 50, unique = true)
        String username
) {
    // (?=.*[a-zA-Z]) : 적어도 하나의 영어 문자가 포함되어야 함
    // [a-zA-Z0-9]{4,50} : 영어 대소문자와 숫자만 허용하며, 4 ~ 50자의 길이여야 함
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])[a-zA-Z0-9]{4,50}$");

    public static Username from(String username) {
        String removeWhitespace = StringUtils.removeWhitespace(username);
        validate(removeWhitespace);
        return new Username(removeWhitespace);
    }

    private static void validate(String username) {
        boolean matches = USERNAME_PATTERN.matcher(username).matches();
        if (!matches) {
            throw new ApplicationException(INVALID_USERNAME);
        }
    }
}
