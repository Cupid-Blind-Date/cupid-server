package cupid.member.domain;

import static com.navercorp.fixturemonkey.api.expression.JavaGetterMethodPropertySelector.javaGetter;
import static cupid.member.exception.MemberExceptionCode.INVALID_CREDENTIALS;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import cupid.support.UnitTest;

@DisplayName("회원 (Member) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberTest extends UnitTest {

    @Test
    void 로그인_시_비밀번호가_일치하면_성공() {
        // given
        Member member = sut.giveMeBuilder(Member.class)
                .set(javaGetter(Member::getPassword), Password.hashPassword("Password1234@"))
                .sample();

        // when & then
        Assertions.assertDoesNotThrow(() -> {
            member.login("Password1234@");
        });
    }

    @Test
    void 로그인_시_비밀번호가_다르면_실패() {
        // given
        Member member = sut.giveMeBuilder(Member.class)
                .set(javaGetter(Member::getPassword), Password.hashPassword("Password1234@"))
                .sample();

        // when & then
        exceptionTest(() -> member.login("Password1234@!"), INVALID_CREDENTIALS);
    }
}
