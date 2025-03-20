package cupid.member.domain.service;

import static cupid.member.exception.MemberExceptionCode.DUPLICATE_USERNAME;
import static org.mockito.BDDMockito.given;

import cupid.member.domain.Member;
import cupid.member.domain.MemberRepository;
import cupid.support.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("MemberRegister 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberRegisterTest extends UnitTest {

    @InjectMocks
    private MemberRegister memberRegister;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void 회원을_등록한다() {
        // given
        Member member = sut.giveMeBuilder(Member.class)
                .sample();
        given(memberRepository.save(member))
                .willReturn(member);

        // when & than
        Assertions.assertDoesNotThrow(() -> {
            memberRegister.register(member);
        });
    }

    @Test
    void 아이디가_중복되었다면_예외() {
        // given
        Member member = sut.giveMeBuilder(Member.class)
                .sample();
        given(memberRepository.save(member))
                .willThrow(DataIntegrityViolationException.class);

        // when & than
        exceptionTest(() -> {
            memberRegister.register(member);
        }, DUPLICATE_USERNAME);
    }
}
