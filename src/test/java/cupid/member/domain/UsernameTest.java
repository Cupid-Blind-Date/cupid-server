package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.INVALID_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cupid.common.exception.ApplicationException;
import cupid.common.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("아이디 (Username) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class UsernameTest {

    @ParameterizedTest
    @ValueSource(ints = {
            4, 5, 9, 10, 11, 20, 30, 40, 49, 50
    })
    void 네글자_이상_50글자_이내여야_한다(int length) {
        // given
        String value = "a".repeat(length);

        // when
        Username username = Username.from(value);

        // then
        assertThat(username.username()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            0, 1, 2, 3, 51, 100
    })
    void 네글자_이상_50글자_이내가_아니면_예외(int length) {
        // given
        String value = "a".repeat(length);

        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class,
                () -> Username.from(value)
        ).getCode();
        assertThat(code).isEqualTo(INVALID_USERNAME);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "aaaa",
            "AAAA",
            "AAA1",
    })
    void 영어_숫자만_입력_가능하다(String value) {
        // when
        Username username = Username.from(value);

        // then
        assertThat(username.username()).isEqualTo(value);
    }

    @Test
    void 숫자로만_이루어지면_예외() {
        // given
        String value = "1111";

        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class,
                () -> Username.from(value)
        ).getCode();
        assertThat(code).isEqualTo(INVALID_USERNAME);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abc!",
            "abc❤️❤️",
            "abc!@",
            "abc한글",
    })
    void 영어_숫자_외_다른_문자가_입력되면_예외(String value) {
        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class,
                () -> Username.from(value)
        ).getCode();
        assertThat(code).isEqualTo(INVALID_USERNAME);
    }
}
