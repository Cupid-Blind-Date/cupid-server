package cupid.async.pinning;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.TaskExecutorAdapter;

@Configuration
public class PinningVirtualThreadConfig {

    public static final String PINNING_VIRTUAL_THREAD_ASYNC_TASK_EXECUTOR = "pinningVirtualThreadAsyncTaskExecutor";

    /**
     * 가상 스레드 설정
     */
    @Bean(name = PINNING_VIRTUAL_THREAD_ASYNC_TASK_EXECUTOR)
    public Executor pinningVirtualThreadAsyncTaskExecutor() {
        ExecutorService concurrentExecutor = Executors.newVirtualThreadPerTaskExecutor();
        return new TaskExecutorAdapter(concurrentExecutor);
    }
}
