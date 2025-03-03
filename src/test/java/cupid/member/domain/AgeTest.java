package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.INVALID_AGE;
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

@DisplayName("나이 (Age) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class AgeTest {

    @Test
    void 음수면_예외() {
        // given
        int value = -1;

        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class,
                () -> Age.from(value)
        ).getCode();
        assertThat(code).isEqualTo(INVALID_AGE);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            0, 1, 20, 99, 100,
    })
    void 나이는_0_이상의_정수여야_한다(int value) {
        // when
        Age age = Age.from(value);

        // then
        assertThat(age.age()).isEqualTo(value);
    }
}
