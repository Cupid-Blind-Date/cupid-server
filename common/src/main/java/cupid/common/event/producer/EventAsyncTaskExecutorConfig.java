package cupid.common.event.producer;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class EventAsyncTaskExecutorConfig {

    public static final String EVENT_ASYNC_TASK_EXECUTOR = "eventAsyncTaskExecutor";

    /**
     * 가상 스레드 사용
     */
    @Bean(name = EVENT_ASYNC_TASK_EXECUTOR)
    public Executor eventAsyncTaskExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.setThreadNamePrefix("VP-");
        executor.setVirtualThreads(true);
        executor.setTaskTerminationTimeout(30000);  // 30 초
        return executor;
    }
}
