package test.async.virtualthread;

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
@RequestMapping("/test/async/virtual-thread")
public class VirtualThreadAsyncController {

    private final AtomicLong blockAtomicId = new AtomicLong(0);
    private final AtomicLong nonBlockAtomicId = new AtomicLong(0);
    private final TestEventTxService eventTxService;

    @GetMapping("/block")
    public ResponseEntity<Void> callBlock() {
        long id = blockAtomicId.incrementAndGet();
        log.info("[block virtual thread] id: {}", id);
        eventTxService.produce(new VirtualThreadBlockEvent(id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/non-block")
    public ResponseEntity<Void> callNonBlock() {
        long id = nonBlockAtomicId.incrementAndGet();
        log.info("[non block virtual thread] id: {}", id);
        eventTxService.produce(new VirtualThreadNonBlockEvent(id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
