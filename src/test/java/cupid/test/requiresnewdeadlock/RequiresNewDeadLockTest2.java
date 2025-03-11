package cupid.test.requiresnewdeadlock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("requiresnewdeadlock2")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class RequiresNewDeadLockTest2 {

    @Autowired
    private TestEventPublisher testEventPublisher;

    @Test
    void 데드락이_발생하지_않는다() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);

        // when
        for (long i = 0; i < 2; i++) {
            final long id = i;
            executorService.submit(() -> {
                testEventPublisher.publishEvent(new TestEvent(id));
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }
}
