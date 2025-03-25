package cupid.recommend.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.TaskExecutorAdapter;

@Configuration
public class RecommendAsyncConfig {

    public static final String RECOMMEND_ASYNC_TASK_EXECUTOR = "recommendEventAsyncTaskExecutor";

    /**
     * 가상 스레드 사용
     */
    @Bean(name = RECOMMEND_ASYNC_TASK_EXECUTOR)
    public Executor recommendEventAsyncTaskExecutor() {
        ExecutorService concurrentExecutor = Executors.newVirtualThreadPerTaskExecutor();
        return new TaskExecutorAdapter(concurrentExecutor);
    }
}
