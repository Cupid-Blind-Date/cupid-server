package cupid.recommend.presentation;

import cupid.common.auth.Auth;
import cupid.recommend.application.RecommendService;
import cupid.recommend.query.result.RecommendedProfile;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/recommends")
@RestController
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping
    public ResponseEntity<RecommendedProfile> recommend(
            @Auth Long memberId,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude
    ) {
        Optional<RecommendedProfile> recommend = recommendService.recommend(memberId, latitude, longitude);
        return ResponseEntity.ok(recommend.orElse(null));
    }
}
