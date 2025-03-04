package cupid.fixture;

import cupid.member.application.command.SignUpCommand;
import cupid.member.domain.Gender;

public class MemberFixture {

    public static SignUpCommand donghunSignupCommand() {
        return new SignUpCommand(
                "donghun",
                "Password1234@",
                "donghun",
                25,
                Gender.MALE
        );
    }

    public static SignUpCommand minjeongSignupCommand() {
        return new SignUpCommand(
                "minjeong",
                "Password1234@",
                "minjeong",
                24,
                Gender.FEMALE
        );
    }

    public static SignUpCommand invalidPasswordSignupCommand() {
        return new SignUpCommand(
                "username",
                "invalid",
                "invalid",
                24,
                Gender.FEMALE
        );
    }
}
