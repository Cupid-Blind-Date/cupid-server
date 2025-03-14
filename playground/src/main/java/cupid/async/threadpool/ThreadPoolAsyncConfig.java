package cupid.async.threadpool;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolAsyncConfig {

    public static final String THREAD_POOL_ASYNC_TASK_EXECUTOR = "threadPoolAsyncTaskExecutor";

    /**
     * 스레드풀 스레드 설정
     */
    @Bean(name = THREAD_POOL_ASYNC_TASK_EXECUTOR)
    public Executor threadPoolAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(200);
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(100_000);
        executor.setThreadNamePrefix("TPool-");
        executor.initialize();
        return executor;
    }
}
