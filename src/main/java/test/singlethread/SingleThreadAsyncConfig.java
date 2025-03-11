package test.singlethread;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class SingleThreadAsyncConfig {

    public static final String SINGLE_THREAD_ASYNC_TASK_EXECUTOR = "singleThreadAsyncTaskExecutor";

    /**
     * 싱글 스레드 설정
     */
    @Bean(name = SINGLE_THREAD_ASYNC_TASK_EXECUTOR)
    public Executor singleThreadAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(5000);
        executor.setThreadNamePrefix("EventAsyncSingleThread-");
        executor.initialize();
        return executor;
    }
}
