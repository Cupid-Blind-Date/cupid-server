package cupid.member.domain.service;

import static cupid.member.exception.MemberExceptionCode.DUPLICATE_USERNAME;

import cupid.common.exception.ApplicationException;
import cupid.member.domain.Member;
import cupid.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberRegister {

    private final MemberRepository memberRepository;

    public Member register(Member member) {
        try {
            return memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(DUPLICATE_USERNAME);
        }
    }
}
