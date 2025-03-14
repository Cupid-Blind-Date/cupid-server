package cupid.async.pinning;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test/pinning")
public class PinningController {

    private final PinningTestService pinningTestService;

    @GetMapping("/blocking")
    public void blocking() {
        pinningTestService.blocking();
    }

    @GetMapping("/cpu")
    public void cpuBoundWork() {
        pinningTestService.cpuBoundWork();
    }

    @GetMapping("/no-synchronized/blocking")
    public void noSynchronizedBlocking() {
        pinningTestService.noSynchronizedBlocking();
    }
}
