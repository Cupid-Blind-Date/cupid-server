package cupid.member.application.command;

import cupid.member.domain.Gender;
import cupid.member.domain.Member;
import cupid.member.domain.ProfileImage;
import cupid.member.domain.RepresentativeProfileImage;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SignUpCommand(
        String username,
        String password,
        String nickname,
        int age,
        Gender gender,
        List<ProfileImageCommand> profileImages
) {
    public Member toMember() {
        ProfileImageCommand representative = profileImages.getFirst();
        RepresentativeProfileImage profileImage = new RepresentativeProfileImage(
                representative.originalName,
                representative.blurredName
        );
        return new Member(username, password, nickname, age, gender, profileImage);
    }

    public List<ProfileImage> toProfileImages(Member member) {
        return profileImages.stream()
                .map(it -> new ProfileImage(member, it.originalName, it.blurredName))
                .toList();
    }

    public record ProfileImageCommand(
            @NotEmpty(message = "사진 이름이 없습니다.")
            String originalName,
            @NotEmpty(message = "블러링된 사진 이름이 없습니다.")
            String blurredName
    ) {
    }
}
