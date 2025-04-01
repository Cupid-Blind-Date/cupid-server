package cupid.fixture;

import cupid.member.application.command.SignUpCommand;
import cupid.member.application.command.SignUpCommand.ProfileImageCommand;
import cupid.member.domain.Gender;
import cupid.member.domain.Member;
import cupid.member.domain.RepresentativeProfileImage;
import java.util.List;
import java.util.UUID;

public class MemberFixture {

    public static Member member(int age, Gender gender) {
        return new Member(
                UUID.randomUUID().toString(),
                "Password1234@",
                "donghun",
                age,
                gender,
                new RepresentativeProfileImage(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        );
    }

    public static SignUpCommand donghunSignupCommand() {
        return new SignUpCommand(
                "donghun",
                "Password1234@",
                "donghun",
                25,
                Gender.MALE,
                List.of(profileImageCommand())
        );
    }

    public static SignUpCommand minjeongSignupCommand() {
        return new SignUpCommand(
                "minjeong",
                "Password1234@",
                "minjeong",
                24,
                Gender.FEMALE,
                List.of(profileImageCommand())
        );
    }

    public static SignUpCommand invalidPasswordSignupCommand() {
        return new SignUpCommand(
                "username",
                "invalid",
                "invalid",
                24,
                Gender.FEMALE,
                List.of(profileImageCommand())
        );
    }

    public static ProfileImageCommand profileImageCommand() {
        return new ProfileImageCommand(UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }
}
