package cupid.evnet;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class EventAsyncConfig {

    public static final String EVENT_ASYNC_TASK_EXECUTOR = "eventAsyncTaskExecutor";

    /**
     * 이벤트를 순차 발행을 위해 싱글 스레드로 동작시킨다.
     * TODO 순차 발행과 싱글 스레드 테스트
     */
    @Bean(name = EVENT_ASYNC_TASK_EXECUTOR)
    public Executor eventAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("EventAsyncThread-");
        executor.initialize();
        return executor;
    }
}
