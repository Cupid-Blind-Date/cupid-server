package cupid.member.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank(message = "아이디는 공백이어서는 안됩니다.")
        @NotNull(message = "아이디는 null 이어서는 안됩니다.")
        String username,

        @NotBlank(message = "비밀번호는 공백이어서는 안됩니다.")
        @NotNull(message = "비밀번호는 null 이어서는 안됩니다.")
        String password
) {
}
