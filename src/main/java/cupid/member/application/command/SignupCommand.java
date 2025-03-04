package cupid.member.application.command;

import cupid.member.domain.Gender;
import cupid.member.domain.Member;

public record SignupCommand(
        String username,
        String password,
        String nickname,
        int age,
        Gender gender
) {
    public Member toMember() {
        return new Member(username, password, nickname, age, gender);
    }
}
