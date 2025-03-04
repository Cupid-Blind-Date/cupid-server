package cupid.member.application;

import cupid.common.exception.ApplicationException;
import cupid.member.application.command.SignUpCommand;
import cupid.member.domain.Member;
import cupid.member.domain.MemberRepository;
import cupid.member.domain.service.MemberRegister;
import cupid.member.exception.MemberExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberRegister memberRegister;

    public Long signUp(SignUpCommand command) {
        Member member = command.toMember();
        Member registeredMember = memberRegister.register(member);
        return registeredMember.getId();
    }

    public Long login(String username, String password) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> {
            throw new ApplicationException(MemberExceptionCode.INVALID_CREDENTIALS);
        });
        member.login(password);
        return member.getId();
    }
}
