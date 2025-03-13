package test.async.pinning;

import static test.async.pinning.PinningVirtualThreadConfig.PINNING_VIRTUAL_THREAD_ASYNC_TASK_EXECUTOR;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PinningTestService {

    public final SynchronizedWorker worker;

    @Async(PINNING_VIRTUAL_THREAD_ASYNC_TASK_EXECUTOR)
    public void blocking() {
        worker.blockingSynchronized();
    }

    @Async(PINNING_VIRTUAL_THREAD_ASYNC_TASK_EXECUTOR)
    public void cpuBoundWork() {
        worker.nonBlockingSynchronized();
    }

    @Async(PINNING_VIRTUAL_THREAD_ASYNC_TASK_EXECUTOR)
    public void noSynchronizedBlocking() {
        worker.noSynchronizedBlocking();
    }
}
