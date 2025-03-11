package test.async.singlethread;

import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.async.event.TestEventTxService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/test/async/single-thread")
public class SingleThreadAsyncController {

    private final AtomicLong atomicId = new AtomicLong(0);
    private final TestEventTxService eventTxService;

    @GetMapping
    public ResponseEntity<Void> call() {
        long id = atomicId.incrementAndGet();
        log.info("[single thread] id: {}", id);
        eventTxService.produce(new SingleThreadEvent(id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
