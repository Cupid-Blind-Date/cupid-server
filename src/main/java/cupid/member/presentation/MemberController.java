package cupid.member.presentation;

import cupid.member.application.MemberService;
import cupid.member.application.command.SignUpCommand;
import cupid.member.presentation.request.SignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public void signUp(
            @RequestBody @Valid SignUpRequest request
    ) {
        SignUpCommand command = request.toCommand();
        memberService.signUp(command);
    }
}
