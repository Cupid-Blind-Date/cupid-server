package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.INVALID_NICKNAME;

import cupid.common.exception.ApplicationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record Nickname(
        @Column(nullable = false, length = 10, unique = false)
        String nickname
) {
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9]{2,10}$");

    public static Nickname from(String nickname) {
        String trimNickname = nickname.trim();
        validate(trimNickname);
        return new Nickname(trimNickname);
    }

    private static void validate(String nickname) {
        boolean matches = NICKNAME_PATTERN.matcher(nickname).matches();
        if (!matches) {
            throw new ApplicationException(INVALID_NICKNAME);
        }
    }
}
