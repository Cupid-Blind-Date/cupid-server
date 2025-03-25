package cupid.filter.presentation;

import cupid.common.auth.Auth;
import cupid.filter.application.FilterService;
import cupid.filter.presentation.request.FilterCreateRequest;
import cupid.filter.presentation.request.FilterUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/filters")
@RestController
public class FilterController {

    private final FilterService filterService;

    @GetMapping("/exists")
    public ResponseEntity<Boolean> hasFilter(
            @Auth Long memberId
    ) {
        boolean hasFilter = filterService.hasFilter(memberId);
        return ResponseEntity.ok(hasFilter);
    }

    @PostMapping
    public void createFilter(
            @Auth Long memberId,
            @RequestBody FilterCreateRequest request
    ) {
        filterService.createFilter(request.toCommand(memberId));
    }

    @PutMapping
    public void updateFilter(
            @Auth Long memberId,
            @RequestBody FilterUpdateRequest request
    ) {
        filterService.updateFilter(request.toCommand(memberId));
    }
}
