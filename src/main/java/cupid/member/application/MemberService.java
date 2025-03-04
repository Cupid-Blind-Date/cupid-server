package cupid.member.application;

import cupid.member.application.command.SignupCommand;
import cupid.member.domain.Member;
import cupid.member.domain.service.MemberRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRegister memberRegister;

    public Long signup(SignupCommand command) {
        Member member = command.toMember();
        Member registeredMember = memberRegister.register(member);
        return registeredMember.getId();
    }
}
