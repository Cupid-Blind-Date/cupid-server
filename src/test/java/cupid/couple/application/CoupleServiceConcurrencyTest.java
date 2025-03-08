package cupid.couple.application;

import cupid.couple.application.command.ShootArrowCommand;
import cupid.support.ApplicationTest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("커플 매칭 동시성 테스트")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class CoupleServiceConcurrencyTest extends ApplicationTest {

    @Autowired
    private CoupleService coupleService;

    // Duplicate Couple creation detected due to simultaneous likes. 로그 확인
    @Disabled
    @Test
    void test() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch countDownLatch = new CountDownLatch(1800);

        // when
        for (long i = 2; i < 1000; i++) {
            ShootArrowCommand c1 = new ShootArrowCommand(1L, i);
            ShootArrowCommand c2 = new ShootArrowCommand(i, 1L);
            executorService.submit(() -> {
                coupleService.shootLikeArrow(c1);
                countDownLatch.countDown();
            });
            executorService.submit(() -> {
                coupleService.shootLikeArrow(c2);
                countDownLatch.countDown();
            });
        }

        // then
        countDownLatch.await();
    }
}
