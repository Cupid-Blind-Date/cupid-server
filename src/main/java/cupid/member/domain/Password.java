package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.INVALID_PASSWORD;
import static java.nio.charset.StandardCharsets.UTF_8;

import cupid.common.exception.ApplicationException;
import jakarta.persistence.Column;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public record Password(
        @Column(nullable = false, length = 50, unique = false)
        String hashedPassword
) {
    // (?!.*\\s) : 공백(띄어쓰기, 탭 등)이 포함되지 않아야 함
    // (?=.*[a-z]) : 소문자(a-z) 최소 1개 포함
    // (?=.*[A-Z]) : 대문자(A-Z) 최소 1개 포함
    // (?=.*\\d) : 숫자(0-9) 최소 1개 포함
    // (?=.*[!@#,.<>?]) : 허용된 특수문자(!@#,.<>?) 중 하나 이상 포함
    // [A-Za-z\\d!@#,.<>?] : 영어, 숫자, 허용된 특수문자만 사용 가능
    // {8,50} : 비밀번호 길이 8~50자
    private static final String REGEX = "^(?!.*\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#,.<>?])[A-Za-z\\d!@#,.<>?]{8,50}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(REGEX);

    public static Password hashPassword(String password) {
        validate(password);
        return new Password(hash(password));
    }

    private static void validate(String password) {
        boolean matches = PASSWORD_PATTERN.matcher(password).matches();
        if (!matches) {
            throw new ApplicationException(INVALID_PASSWORD);
        }
    }

    private static String hash(String password) {
        try {
            // MessageDigest is not thread safe
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean checkPassword(String plainPassword) {
        return this.hashedPassword.equals(hash(plainPassword));
    }
}
