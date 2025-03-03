package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.INVALID_NICKNAME;
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

@DisplayName("닉네임 (Nickname) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class NicknameTest {

    @ParameterizedTest
    @ValueSource(ints = {
            2, 3, 4, 5, 6, 7, 8, 9, 10
    })
    void 두글자_이상_10글자_이내여야_한다(int length) {
        // given
        String value = "a".repeat(length);

        // when
        Nickname nickname = Nickname.from(value);

        // then
        assertThat(nickname.nickname()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1, 11, 20, 100
    })
    void 두글자_이상_10글자_이내가_아니면_예외(int length) {
        // given
        String value = "a".repeat(length);

        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class,
                () -> Nickname.from(value)
        ).getCode();
        assertThat(code).isEqualTo(INVALID_NICKNAME);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "aa",
            "AA",
            "11",
            "가가",
            "aA1가",
    })
    void 한글_영어_숫자만_입력_가능하다(String value) {
        // when
        Nickname nickname = Nickname.from(value);

        // then
        assertThat(nickname.nickname()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "!!",
            "❤️❤️",
            "a@",
            "@a",
            "a@a",
    })
    void 한글_영어_숫자_이외_다른_문자가_입력되면_예외(String value) {
        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class,
                () -> Nickname.from(value)
        ).getCode();
        assertThat(code).isEqualTo(INVALID_NICKNAME);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "안 녕,안녕",
            "  안  녕  ,안녕",
    }, delimiterString = ",")
    void 공백이_들어오면_제거된다(String input, String expected) {
        // when
        Nickname actual = Nickname.from(input);

        // then
        assertThat(actual.nickname()).isEqualTo(expected);
    }
}
