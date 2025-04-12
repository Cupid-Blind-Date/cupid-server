package cupid.member.application;


import static cupid.member.exception.MemberExceptionCode.DUPLICATE_USERNAME;
import static cupid.member.exception.MemberExceptionCode.INVALID_CREDENTIALS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cupid.common.value.Point;
import cupid.fixture.MemberFixture;
import cupid.member.application.command.SignUpCommand;
import cupid.member.domain.MemberRepository;
import cupid.member.domain.RecentActiveInfo;
import cupid.support.ApplicationTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class 회원가입_시 {

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

    @Nested
    class 로그인_시 {

        private final SignUpCommand command = MemberFixture.donghunSignupCommand();

        @BeforeEach
        void setUp() {
            memberService.signUp(command);
        }

        @Test
        void 아이디가_없으면_예외() {
            // when & then
            exceptionTest(() -> {
                memberService.login("invalid", command.password());
            }, INVALID_CREDENTIALS);
        }

        @Test
        void 비밀번호가_틀리면_예외() {
            // when & then
            exceptionTest(() -> {
                memberService.login(command.username(), "invalid");
            }, INVALID_CREDENTIALS);
        }

        @Test
        void 로그인_성공() {
            // when & then
            assertDoesNotThrow(() -> {
                memberService.login(command.username(), command.password());
            });
        }
    }

    @Test
    void 최근_활동상태_업데이트() {
        // given
        SignUpCommand command = MemberFixture.donghunSignupCommand();
        Long memberId = memberService.signUp(command);
        RecentActiveInfo recentActiveInfo = new RecentActiveInfo(LocalDateTime.now(), new Point(10.0, -10.0));

        // when
        memberService.updateRecentActiveInfo(memberId, recentActiveInfo);

        // then
        RecentActiveInfo updated = memberRepository.getById(memberId).getRecentActiveInfo();
        assertThat(updated.getLastActiveDate()).isNotNull();
        assertThat(updated.getPoint().getLatitude()).isEqualTo(BigDecimal.valueOf(10.0));
        assertThat(updated.getPoint().getLongitude()).isEqualTo(BigDecimal.valueOf(-10.0));
    }
}
