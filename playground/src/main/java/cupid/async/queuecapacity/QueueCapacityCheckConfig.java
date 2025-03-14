package cupid.async.queuecapacity;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class QueueCapacityCheckConfig {

    public static final String QUEUE_CAPACITY_CHECK_TASK_EXECUTOR = "queueCapacityCheckTaskExecutor";

    /**
     * 스레드 2개 설정, Queue 개수 10개로 제한
     */
    @Bean(name = QUEUE_CAPACITY_CHECK_TASK_EXECUTOR)
    public Executor queueCapacityCheckTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("QCapacityCheck-");
        executor.initialize();
        return executor;
    }
}
