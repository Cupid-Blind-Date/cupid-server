package cupid.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("비밀번호 (Password) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PasswordTest {

    @Test
    void 비밀번호는_해시된다() {
        // given
        String value = "Aa123!@#dsa";

        // when
        Password password = Password.hashPassword(value);

        // then
        assertThat(password.getHashedPassword()).isNotEqualTo(value);
    }

    @Test
    void 비밀번호_일치여부_검증() {
        // given
        String value = "Aa123!@#dsa";
        Password password = Password.hashPassword(value);

        // when
        boolean actualTrue = password.checkPassword(value);
        boolean actualFalse = password.checkPassword(value + "1");

        // then
        assertThat(actualTrue).isEqualTo(true);
        assertThat(actualFalse).isEqualTo(false);
    }
}
