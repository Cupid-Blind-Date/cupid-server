package cupid.recommend.presentation;

import cupid.common.auth.Auth;
import cupid.recommend.application.RecommendService;
import cupid.recommend.query.result.RecommendedProfile;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/recommends")
@RestController
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping
    public ResponseEntity<RecommendedProfile> recommend(
            @Auth Long memberId
    ) {
        Optional<RecommendedProfile> recommend = recommendService.recommend(memberId);
        return ResponseEntity.ok(recommend.orElse(null));
    }
}
