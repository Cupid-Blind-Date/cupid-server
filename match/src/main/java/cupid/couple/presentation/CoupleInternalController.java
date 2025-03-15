package cupid.couple.presentation;

import cupid.couple.domain.Couple;
import cupid.couple.domain.CoupleRepository;
import cupid.couple.presentation.response.GetCoupleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/couples")
public class CoupleInternalController {

    private final CoupleRepository coupleRepository;

    @GetMapping("/{id}")
    public ResponseEntity<GetCoupleResponse> getCoupleById(
            @PathVariable(name = "id") Long id
    ) {
        log.info("Try to get couple by id request. id: {}", id);
        Couple couple = coupleRepository.getById(id);
        GetCoupleResponse response = GetCoupleResponse.from(couple);
        log.info("Successfully get couple by id. GetCoupleResponse: {}", response);
        return ResponseEntity.ok(response);
    }
}
