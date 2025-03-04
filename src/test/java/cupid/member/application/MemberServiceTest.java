package cupid.member.application;


import static cupid.member.exception.MemberExceptionCode.DUPLICATE_USERNAME;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cupid.fixture.MemberFixture;
import cupid.member.application.command.SignUpCommand;
import cupid.member.domain.MemberRepository;
import cupid.support.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayName("MemberService 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberServiceTest extends ApplicationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원가입_테스트() {
        // given
        SignUpCommand command = MemberFixture.donghunSignupCommand();

        // when
        Long id = memberService.signUp(command);

        // then
        assertDoesNotThrow(() -> {
            memberRepository.getById(id);
        });
    }

    @Test
    void 아이디_중복시_예외() {
        // given
        SignUpCommand command = MemberFixture.donghunSignupCommand();
        SignUpCommand duplicatedCommand = MemberFixture.donghunSignupCommand();
        memberService.signUp(command);

        // when & then
        exceptionTest(() -> memberService.signUp(duplicatedCommand),
                DUPLICATE_USERNAME);
    }
}
