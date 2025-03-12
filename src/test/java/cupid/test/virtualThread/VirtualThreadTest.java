package cupid.test.virtualThread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

@Disabled
@DisplayName("VirtualThread 관련 테스트")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class VirtualThreadTest {

    @Test
    void 가상_스레드_내에서_CompletableFuture_get_호출() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        CountDownLatch countDownLatch = new CountDownLatch(20);
        StopWatch stopWatch = new StopWatch();

        // when
        stopWatch.start();
        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                try {
                    CompletableFuture.supplyAsync(() -> {
                                try {
                                    Thread.sleep(1000);
                                    countDownLatch.countDown();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                return "hi";
                            })
                            .get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // then
        countDownLatch.await();
        stopWatch.stop();
        double totalTime = stopWatch.getTotalTime(TimeUnit.MILLISECONDS);
        double totalTime1 = stopWatch.getTotalTime(TimeUnit.SECONDS);
        System.out.println(totalTime);
        System.out.println(totalTime1);
    }

    // 스레드풀이 모두 사용중이면 대기해야 하므로 성능이 느려진다.
    @Test
    void 스레드풀_내에서_CompletableFuture_get_호출() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(20);
        StopWatch stopWatch = new StopWatch();

        // when
        stopWatch.start();
        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                try {
                    CompletableFuture.supplyAsync(() -> {
                                try {
                                    Thread.sleep(1000);
                                    countDownLatch.countDown();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                return "hi";
                            })
                            .get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // then
        countDownLatch.await();
        stopWatch.stop();
        double totalTime = stopWatch.getTotalTime(TimeUnit.MILLISECONDS);
        double totalTime1 = stopWatch.getTotalTime(TimeUnit.SECONDS);
        System.out.println(totalTime);
        System.out.println(totalTime1);
    }
}
