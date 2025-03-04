package cupid.member.application;


import static cupid.member.exception.MemberExceptionCode.DUPLICATE_USERNAME;
import static cupid.member.exception.MemberExceptionCode.INVALID_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cupid.fixture.MemberFixture;
import cupid.member.application.command.SignupCommand;
import cupid.member.domain.MemberRepository;
import cupid.support.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayName(" (MemberService) 은(는)")
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
        SignupCommand command = MemberFixture.donghunSignupCommand();

        // when
        Long id = memberService.signup(command);

        // then
        assertDoesNotThrow(() -> {
            memberRepository.getById(id);
        });
    }

    @Test
    void 아이디_중복시_예외() {
        // given
        SignupCommand command = MemberFixture.donghunSignupCommand();
        SignupCommand duplicatedCommand = MemberFixture.donghunSignupCommand();
        memberService.signup(command);

        // when & then
        exceptionTest(() -> memberService.signup(duplicatedCommand),
                DUPLICATE_USERNAME);
    }

    @Test
    void 입력정보가_유효하지_않은_경우_예외() {
        // given
        SignupCommand command = MemberFixture.invalidPasswordSignupCommand();

        // when & then
        exceptionTest(() -> memberService.signup(command),
                INVALID_PASSWORD);
    }
}
