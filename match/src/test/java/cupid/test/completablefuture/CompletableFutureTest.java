package cupid.test.completablefuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("CompletableFuture 학습 테스트")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class CompletableFutureTest {

    @Test
    void whenComplete_는_예외처리_후에도_예외가_전파된다() {
        // given
        Assertions.assertThatThrownBy(() -> {
            CompletableFuture.supplyAsync(() -> {
                if (true) {
                    throw new RuntimeException("EX");
                }
                return "hi";
            }).whenComplete((returnValue, throwable) -> {
                if (throwable != null) {
                    System.out.println("Exception occur");
                } else {
                    System.out.println("Success");
                }
            }).get();
        }).isInstanceOf(ExecutionException.class);
    }

    @Test
    void handle은_예외를_잡을_수_있다() {
        // given
        assertDoesNotThrow(() -> {
            CompletableFuture.supplyAsync(() -> {
                if (true) {
                    throw new RuntimeException("EX");
                }
                return "hi";
            }).handle((returnValue, throwable) -> {
                if (throwable != null) {
                    System.out.println("Exception occur");
                    return "f";
                } else {
                    System.out.println("Success");
                    return "s";
                }
            }).get();
        });
    }

    @Test
    void hadnle_과정에서_예외가_발생하면_전파된다() {
        // given
        Assertions.assertThatThrownBy(() -> {
            CompletableFuture.supplyAsync(() -> {
                if (true) {
                    throw new RuntimeException("EX");
                }
                return "hi";
            }).handle((returnValue, throwable) -> {
                if (throwable != null) {
                    System.out.println("Exception occur");
                    throw new RuntimeException("EX2");
                } else {
                    System.out.println("Success");
                    return "hi";
                }
            }).get();
        }).isInstanceOf(ExecutionException.class);
    }

    @Test
    void exceptionally는_예외에만_접근할_수_있다() {
        // given
        assertDoesNotThrow(() -> {
            CompletableFuture.supplyAsync(() -> {
                if (true) {
                    throw new RuntimeException("EX");
                }
                return "hi";
            }).exceptionally((throwable) -> {
                if (throwable != null) {
                    System.out.println("Exception occur");
                    return "f";
                } else {
                    System.out.println("Success");
                    return "s";
                }
            }).get();
        });
    }

    @Test
    void exceptionally는_예외가_없는_경우_동작하지_않는다() {
        // given
        assertDoesNotThrow(() -> {
            CompletableFuture.supplyAsync(() -> "hi").exceptionally((throwable) -> {
                System.out.println("Unreachable");
                throw new RuntimeException("");
            }).get();
        });
    }

    @Test
    void exceptionally는_예외를_잡을_수_있다() {
        // given
        assertDoesNotThrow(() -> {
            CompletableFuture.supplyAsync(() -> {
                if (true) {
                    throw new RuntimeException("EX");
                }
                return "hi";
            }).exceptionally((throwable) -> {
                System.out.println("reachable");
                return "";
            }).get();
        });
    }

    @Test
    void whenComplete에서_또_예외_발생시_아무런_작업이_일어나지_않는다() {
        Assertions.assertThatThrownBy(() -> {
            CompletableFuture.supplyAsync(() -> {
                if (true) {
                    throw new RuntimeException("EX");
                }
                return "hi";
            }).whenComplete((s, throwable) -> {
                if (throwable != null) {
                    System.out.println("!!!!!!!1");
                    throw new RuntimeException("Other!");
                }
            }).get();
        });
    }

    @Test
    void get_으로_대기하는_경우_예외가_발생하면_Try_Catch로_잡을_수_있다() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        assertDoesNotThrow(() -> {
            try {
                CompletableFuture.failedFuture(new RuntimeException("Fail")).get();
                System.out.println("Unreachable");
            } catch (Exception e) {
                System.out.println("Exception!");
                atomicInteger.getAndIncrement();
            }
        });
        assertThat(atomicInteger.get()).isEqualTo(2);
    }
}
