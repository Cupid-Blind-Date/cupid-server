package cupid.couple.presentation;

import cupid.auth.Auth;
import cupid.couple.application.CoupleService;
import cupid.couple.application.command.LikeCommand;
import cupid.couple.domain.service.MatchResult;
import cupid.couple.presentation.request.LikeRequest;
import cupid.couple.presentation.response.LikeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/couples")
public class CoupleController {

    private final CoupleService coupleService;

    /**
     * 좋아요 요청
     */
    @PostMapping("/like")
    public ResponseEntity<LikeResponse> like(
            @Auth Long memberId,
            @RequestBody @Valid LikeRequest request
    ) {
        LikeCommand command = request.toCommand(memberId);
        MatchResult result = coupleService.like(command);
        return ResponseEntity.ok(new LikeResponse(result));
    }

    /**
     * 싫어요 요청
     */
    @PostMapping("/dislike")
    public void dislike(
            @Auth Long memberId,
            @RequestBody @Valid LikeRequest request
    ) {
        LikeCommand command = request.toCommand(memberId);
        coupleService.dislike(command);
    }
}
