package cupid.fixture;

import cupid.member.application.command.SignupCommand;
import cupid.member.domain.Gender;

public class MemberFixture {

    public static SignupCommand donghunSignupCommand() {
        return new SignupCommand(
                "donghun",
                "Password1234@",
                "donghun",
                25,
                Gender.MALE
        );
    }

    public static SignupCommand minjeongSignupCommand() {
        return new SignupCommand(
                "minjeong",
                "Password1234@",
                "minjeong",
                24,
                Gender.FEMALE
        );
    }

    public static SignupCommand invalidPasswordSignupCommand() {
        return new SignupCommand(
                "username",
                "invalid",
                "invalid",
                24,
                Gender.FEMALE
        );
    }
}
