package cupid.member.application;

import cupid.member.application.command.SignUpCommand;
import cupid.member.domain.Member;
import cupid.member.domain.service.MemberRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRegister memberRegister;

    public Long signUp(SignUpCommand command) {
        Member member = command.toMember();
        Member registeredMember = memberRegister.register(member);
        return registeredMember.getId();
    }
}
