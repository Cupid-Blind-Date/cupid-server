package cupid.member.presentation.request;

import cupid.member.application.command.SignUpCommand;
import cupid.member.application.command.SignUpCommand.ProfileImageCommand;
import cupid.member.domain.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record SignUpRequest(
        // (?!.*\\s) : 공백(띄어쓰기, 탭 등)이 포함되지 않아야 함
        // (?=.*[a-zA-Z]) : 적어도 하나의 영어 문자가 포함되어야 함
        // [a-zA-Z0-9]{4,50} : 영어 대소문자와 숫자만 허용하며, 4 ~ 50자의 길이여야 함
        @Pattern(
                regexp = "^(?!.*\\s)(?=.*[a-zA-Z])[a-zA-Z0-9]{4,50}$",
                message = "아이디 형식이 올바르지 않습니다."
        )
        @NotNull(message = "아이디는 null 이어서는 안됩니다.")
        String username,

        // (?!.*\\s) : 공백(띄어쓰기, 탭 등)이 포함되지 않아야 함
        // (?=.*[a-z]) : 소문자(a-z) 최소 1개 포함
        // (?=.*[A-Z]) : 대문자(A-Z) 최소 1개 포함
        // (?=.*\\d) : 숫자(0-9) 최소 1개 포함
        // (?=.*[!@#,.<>?]) : 허용된 특수문자(!@#,.<>?) 중 하나 이상 포함
        // [A-Za-z\\d!@#,.<>?] : 영어, 숫자, 허용된 특수문자만 사용 가능
        // {8,50} : 비밀번호 길이 8~50자
        @Pattern(
                regexp = "^(?!.*\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#,.<>?])[A-Za-z\\d!@#,.<>?]{8,50}$",
                message = "비밀번호 형식이 올바르지 않습니다."
        )
        @NotNull(message = "비밀번호는 null 이어서는 안됩니다.")
        String password,

        // (?!.*\\s) : 공백(띄어쓰기, 탭 등)이 포함되지 않아야 함
        @Pattern(regexp = "^(?!.*\\s)[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임 형식이 올바르지 않습니다.")
        @NotNull(message = "닉네임은 null 이어서는 안됩니다.")
        String nickname,

        @Min(value = 0, message = "나이는 0 이상의 양수여야 합니다.")
        @NotNull(message = "나이는 null 이어서는 안됩니다.")
        Integer age,

        @NotNull(message = "성별은 null 이어서는 안됩니다.")
        Gender gender,

        @Valid
        @NotNull(message = "프로필 사진이 없습니다.")
        @Size(min = 1, max = 6, message = "프로필 사진은 최소 1장에서 최대 6장 사이어야 합니다.")
        List<ProfileImageRequest> profileImages
) {
    public SignUpCommand toCommand() {
        return new SignUpCommand(
                username,
                password,
                nickname,
                age,
                gender,
                profileImages.stream()
                        .map(it -> new ProfileImageCommand(it.originalName, it.blurredName))
                        .toList()
        );
    }

    public record ProfileImageRequest(
            @NotEmpty(message = "사진 이름이 없습니다.")
            String originalName,
            @NotEmpty(message = "블러링된 사진 이름이 없습니다.")
            String blurredName
    ) {
    }
}
