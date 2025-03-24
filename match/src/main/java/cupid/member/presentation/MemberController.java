package cupid.member.presentation;

import cupid.common.auth.Token;
import cupid.common.auth.TokenService;
import cupid.member.application.MemberService;
import cupid.member.application.command.SignUpCommand;
import cupid.member.presentation.request.LoginRequest;
import cupid.member.presentation.request.SignUpRequest;
import cupid.member.presentation.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<LoginResponse> signUp(
            @RequestBody @Valid SignUpRequest request
    ) {
        SignUpCommand command = request.toCommand();
        Long id = memberService.signUp(command);
        Token token = tokenService.createToken(id);
        return ResponseEntity.ok(new LoginResponse(id, token.accessToken()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        Long id = memberService.login(request.username(), request.password());
        Token token = tokenService.createToken(id);
        return ResponseEntity.ok(new LoginResponse(id, token.accessToken()));
    }
}
