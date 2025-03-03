package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.INVALID_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cupid.common.exception.ApplicationException;
import cupid.common.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("비밀번호 (Password) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PasswordTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "Ab1@Ab1@",
            "Ab1@Ab1@1",
            "Aa@!b000001234567890123456789012345678901234567890", // 50글자
    })
    void 여덟글자_이상_50글자_이내여야_한다(String value) {
        // when
        Password password = Password.from(value);

        // then
        assertThat(password.password()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "A",
            "Aa1@",
            "Aa1@567",
            "Aa@!b0000012345678901234567890123456789012345678901", // 51 글자
    })
    void 여덟글자_이상_50글자_이내가_아니면_예외(String value) {
        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class,
                () -> Password.from(value)
        ).getCode();
        assertThat(code).isEqualTo(INVALID_PASSWORD);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Aa1@5678",
    })
    void 영어_숫자_특수문자만_입력_가능하다(String value) {
        // when
        Password password = Password.from(value);

        // then
        assertThat(password.password()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Aa1@567가",
            "Aa1@567❤️",
    })
    void 영어_숫자_특수문자_이외_다른_문자가_입력되면_예외(String value) {
        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class,
                () -> Password.from(value)
        ).getCode();
        assertThat(code).isEqualTo(INVALID_PASSWORD);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "A a 1 @ 1111,Aa1@1111",
            "  A  a 1 @ 1 1  11 ,Aa1@1111",
    }, delimiterString = ",")
    void 공백이_들어오면_제거된다(String input, String expected) {
        // when
        Password actual = Password.from(input);

        // then
        assertThat(actual.password()).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "aaaaaaa1@",  // 대문자 없음
            "AAAAAAA1@",  // 소문자 없음
            "@AAAaaaa@",  // 숫자 없음
            "1AAAaaaa1",  // 숫자 없음
    })
    void 최소_한_개_이상의_숫자_소문자_대문자_특수문자를_포함하지_않으면_예외(String value) {
        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class,
                () -> Password.from(value)
        ).getCode();
        assertThat(code).isEqualTo(INVALID_PASSWORD);
    }
}
