package cupid.gracefulshutdown;


import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class GracefulShutdownThreadConfig {

    @Bean(name = "virtualThreadGracefulShutdown")
    public Executor virtualThreadAsyncTaskExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.setThreadNamePrefix("VP-");
        executor.setVirtualThreads(true);
        executor.setTaskTerminationTimeout(30000);  // 30초
        return executor;
    }


}
