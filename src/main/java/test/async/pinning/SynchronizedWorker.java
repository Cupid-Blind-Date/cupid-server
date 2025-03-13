package test.async.pinning;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SynchronizedWorker {

    public synchronized void blockingSynchronized() {
        log.info("[blockingSynchronized] Hi!");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("[blockingSynchronized] Bi!");
    }

    public synchronized void nonBlockingSynchronized() {
        log.info("[nonBlockingSynchronized] Hi!");
        List<Integer> list = new ArrayList<>();
        int i = 0;
        while (i < 100000) {
            list.add(i);
            i++;
        }
        log.info("[nonBlockingSynchronized] Bi!");
    }

    public void noSynchronizedBlocking() {
        log.info("[no synchronized] Hi!");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("[no synchronized] Bi!");
    }
}
