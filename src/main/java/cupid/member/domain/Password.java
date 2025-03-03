package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.INVALID_PASSWORD;

import cupid.common.exception.ApplicationException;
import cupid.common.utils.StringUtils;
import jakarta.persistence.Column;
import java.util.regex.Pattern;

public record Password(
        @Column(nullable = false, length = 50, unique = false)
        String password
) {
    // (?=.*[a-z]) : 소문자(a-z) 최소 1개 포함
    // (?=.*[A-Z]) : 대문자(A-Z) 최소 1개 포함
    // (?=.*\\d) : 숫자(0-9) 최소 1개 포함
    // (?=.*[!@#,.<>?]) : 허용된 특수문자(!@#,.<>?) 중 하나 이상 포함
    // [A-Za-z\\d!@#,.<>?] : 영어, 숫자, 허용된 특수문자만 사용 가능
    // {8,50} : 비밀번호 길이 8~50자
    private static final String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#,.<>?])[A-Za-z\\d!@#,.<>?]{8,50}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(REGEX);

    public static Password from(String password) {
        String removeWhitespace = StringUtils.removeWhitespace(password);
        validate(removeWhitespace);
        return new Password(removeWhitespace);
    }

    private static void validate(String password) {
        boolean matches = PASSWORD_PATTERN.matcher(password).matches();
        if (!matches) {
            throw new ApplicationException(INVALID_PASSWORD);
        }
    }
}
